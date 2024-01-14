package com.codingbottle.domain.user.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.common.security.WithAuthUser;
import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.domain.user.model.UserNicknameRequest;
import com.codingbottle.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.ContextConfiguration;

import static com.codingbottle.docs.util.ApiDocumentUtils.*;
import static com.codingbottle.fixture.DomainFixture.유저1;
import static com.codingbottle.fixture.DomainFixture.유저_닉네임_변경_요청1;
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

    private static final String REQUEST_URL = "/api/v1/user";

    @Test
    @DisplayName("사용자의 닉네임을 변경한다.")
    @WithAuthUser
    void update_nickname() throws Exception {
        //given
        given(userService.updateNickname(any(UserNicknameRequest.class), any(User.class))).willReturn(유저1);
        //when & then
        mvc.perform(patch(REQUEST_URL + "/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(유저_닉네임_변경_요청1))
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("user-nickname-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        requestFields(
                                fieldWithPath("nickname").description("변경할 닉네임")
                        ),
                        responseFieldsByUser()));
    }

    @Test
    @DisplayName("사용자 정보를 조회한다.")
    @WithAuthUser
    void get_user() throws Exception {
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

    private static ResponseFieldsSnippet responseFieldsByUser() {
        return responseFields(
                fieldWithPath("nickname").description("사용자 이름").type("String"),
                fieldWithPath("region").description("사용자 지역").type("String")
        );
    }

}