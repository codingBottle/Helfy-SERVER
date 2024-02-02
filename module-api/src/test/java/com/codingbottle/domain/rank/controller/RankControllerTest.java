package com.codingbottle.domain.rank.controller;

import com.codingbottle.redis.domain.quiz.service.QuizRankRedisService;
import com.codingbottle.docs.util.RestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.codingbottle.docs.util.ApiDocumentUtils.*;
import static com.codingbottle.fixture.DomainFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("RankController 테스트")
@ContextConfiguration(classes = RankController.class)
@WebMvcTest(value = RankController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class RankControllerTest extends RestDocsTest {
    @MockBean
    private QuizRankRedisService quizRankRedisService;

    private static final String REQUEST_URL = "/api/v1/rank";

    @Test
    @DisplayName("1등부터 10등까지의 랭킹을 조회한다")
    void get_rank() throws Exception {
        //given
        given(quizRankRedisService.getUsersRank()).willReturn(List.of(퀴즈_랭킹_유저1, 퀴즈_랭킹_유저2, 퀴즈_랭킹_유저3));

        given(quizRankRedisService.getRankInfo(퀴즈_랭킹_유저1)).willReturn(유저_랭킹_정보1);
        given(quizRankRedisService.getRankInfo(퀴즈_랭킹_유저2)).willReturn(유저_랭킹_정보2);
        given(quizRankRedisService.getRankInfo(퀴즈_랭킹_유저3)).willReturn(유저_랭킹_정보3);
        //when & then
        mvc.perform(get(REQUEST_URL)
                .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("rank-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        responseFields(
                                fieldWithPath("userInfoWithRankInfoList[].rankInfo").description("사용자 순위 정보"),
                                fieldWithPath("userInfoWithRankInfoList[].rankInfo.rank").description("순위").type("Number"),
                                fieldWithPath("userInfoWithRankInfoList[].rankInfo.score").description("점수").type("Number"),
                                fieldWithPath("userInfoWithRankInfoList[].userInfo").description("사용자 정보"),
                                fieldWithPath("userInfoWithRankInfoList[].userInfo.id").description("사용자 ID").type("Number"),
                                fieldWithPath("userInfoWithRankInfoList[].userInfo.nickname").description("사용자 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("유저의 랭킹을 조회한다")
    void get_user_rank() throws Exception {
        //given
        given(quizRankRedisService.getRankInfo(any())).willReturn(유저_랭킹_정보1);
        //when & then
        mvc.perform(get(REQUEST_URL + "/user")
                .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("user-rank",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        responseFields(
                                fieldWithPath("rankInfo").description("사용자 순위 정보"),
                                fieldWithPath("rankInfo.rank").description("순위").type("Number"),
                                fieldWithPath("rankInfo.score").description("점수").type("Number"),
                                fieldWithPath("userInfo").description("사용자 정보"),
                                fieldWithPath("userInfo.id").description("사용자 ID").type("Number"),
                                fieldWithPath("userInfo.nickname").description("사용자 닉네임")
                        )
                ));
    }
}