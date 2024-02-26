package com.codingbottle.domain.user.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.common.security.WithAuthUser;
import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.domain.user.model.UserInfoUpdateRequest;
import com.codingbottle.domain.user.service.UserService;
import com.codingbottle.redis.domain.quiz.service.QuizRankRedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.ContextConfiguration;

import static com.codingbottle.docs.util.ApiDocumentUtils.*;
import static com.codingbottle.fixture.DomainFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("UserController 테스트")
@ContextConfiguration(classes = UserController.class)
@WebMvcTest(value = UserController.class)
class UserControllerTest extends RestDocsTest {
    @MockBean
    private UserService userService;

    @MockBean
    private QuizRankRedisService quizRankRedisService;

    private static final String REQUEST_URL = "/api/v1/user";

    @Test
    @DisplayName("사용자의 정보를 변경한다.")
    @WithAuthUser
    void update_nickname() throws Exception {
        //given
        given(userService.updateInfo(any(UserInfoUpdateRequest.class), any(User.class))).willReturn(유저1);
        //when & then
        mvc.perform(patch(REQUEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(유저_정보_수정_요청1))
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("user-info-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        requestFields(
                                fieldWithPath("nickname").description("변경할 닉네임"),
                                fieldWithPath("region").description("변경할 지역")
                        ),
                        responseFieldsByUpdateUser()));
    }

    @Test
    @DisplayName("사용자 정보를 조회한다.")
    @WithAuthUser
    void get_user() throws Exception {
        //given
        given(quizRankRedisService.getRankInfo(any())).willReturn(유저_랭킹_정보1);
        //when & then
        mvc.perform(get(REQUEST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer FirebaseToken"))
                .andDo(document("get-user",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        responseFieldsByUser()));
    }

    @Test
    @DisplayName("사용자의 프로필 이미지를 변경한다.")
    @WithAuthUser
    void update_profile_image() throws Exception {
        //given
        given(userService.updateProfileImage(any(User.class), any())).willReturn(유저1);
        //when & then
        mvc.perform(patch(REQUEST_URL + "/image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(유저_프로필_이미지_수정_요청1))
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("user-profile-image-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        requestFields(
                                fieldWithPath("id").description("변경할 이미지 ID")
                        ),
                        responseFieldsByUpdateUser()));
    }

    private static ResponseFieldsSnippet responseFieldsByUser() {
        return responseFields(
                fieldWithPath("userInfo").description("사용자 정보"),
                fieldWithPath("userInfo.nickname").description("사용자 이름").type("String"),
                fieldWithPath("userInfo.region").description("사용자 지역").type("String"),
                fieldWithPath("userInfo.profileImageUrl").description("사용자 프로필 이미지").type("String"),
                fieldWithPath("rankInfo").description("사용자 랭킹 정보"),
                fieldWithPath("rankInfo.rank").description("사용자 퀴즈 랭킹").type("Number"),
                fieldWithPath("rankInfo.score").description("사용자 퀴즈 점수").type("Number")

        );
    }

    private static ResponseFieldsSnippet responseFieldsByUpdateUser() {
        return responseFields(
                fieldWithPath("nickname").description("사용자 이름").type("String"),
                fieldWithPath("region").description("사용자 지역").type("String"),
                fieldWithPath("profileImageUrl").description("사용자 프로필 이미지").type("String")
       );
    }
}