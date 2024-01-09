package com.codingbottle.domain.rank.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.redis.service.QuizRankRedisService;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.given;
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
        given(quizRankRedisService.getUsersRank()).willReturn(List.of(유저_랭킹1, 유저_랭킹2, 유저_랭킹3));
        //when & then
        mvc.perform(get(REQUEST_URL)
                .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("rank-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        responseFields(
                                fieldWithPath("userRankResponses[].rank").description("순위").type("Number"),
                                fieldWithPath("userRankResponses[].nickname").description("닉네임"),
                                fieldWithPath("userRankResponses[].score").description("점수").type("Number")
                        )
                ));
    }

    @Test
    @DisplayName("유저의 랭킹을 조회한다")
    void get_user_rank() throws Exception {
        //given
        given(quizRankRedisService.getUserRank(any(User.class))).willReturn(유저_랭킹1);
        //when & then
        mvc.perform(get(REQUEST_URL + "/user")
                .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("user-rank",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        responseFields(
                                fieldWithPath("rank").description("순위").type("Number"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("score").description("점수").type("Number")
                        )
                ));
    }
}