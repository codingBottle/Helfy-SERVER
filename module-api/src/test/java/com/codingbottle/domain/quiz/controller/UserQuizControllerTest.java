package com.codingbottle.domain.quiz.controller;

import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.quiz.model.QuizStatusRequest;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.domain.quiz.service.UserQuizService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.codingbottle.docs.util.ApiDocumentUtils.*;
import static com.codingbottle.fixture.DomainFixture.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("UserQuizController 테스트")
@ContextConfiguration(classes = UserQuizController.class)
@WebMvcTest(value = UserQuizController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserQuizControllerTest extends RestDocsTest {
    @MockBean
    private UserQuizService userQuizService;

    private static final String REQUEST_URL = "/api/v1/quiz/users";

    @Test
    @DisplayName("사용자 오답 퀴즈를 조회한다")
    void get_wrong_quizzes() throws Exception {
        //given
        given(userQuizService.findRandomWrongQuizzesByUser(any(User.class))).willReturn(List.of(QuizResponse.from(퀴즈1), QuizResponse.from(퀴즈2)));
        //when & then
        mvc.perform(get(REQUEST_URL + "/wrong")
                .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("wrong-answer-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        responseFields(
                                fieldWithPath("[].id").description("퀴즈 ID").type("Number"),
                                fieldWithPath("[].question").description("퀴즈 질문"),
                                fieldWithPath("[].answer").description("퀴즈 정답"),
                                fieldWithPath("[].quizType").description("퀴즈 타입 (MultipleChoice / OX)"),
                                fieldWithPath("[].choices.*").description("퀴즈 보기 (객관식은 4개, OX는 2개)").type("Map"),
                                fieldWithPath("[].image").description("퀴즈 이미지 (null일 수 있음)").optional(),
                                fieldWithPath("[].image.id").description("퀴즈 이미지 id").type("Number"),
                                fieldWithPath("[].image.imageUrl").description("퀴즈 이미지 url")
                        )));
    }

    @Test
    @DisplayName("사용자 퀴즈 정보를 조회한다")
    void get_user_quiz_info() throws Exception {
        //given
        given(userQuizService.getUserQuizInfo(any(User.class))).willReturn(퀴즈정보1);
        //when & then
        mvc.perform(get(REQUEST_URL)
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("get-user-quiz-info",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        responseFields(
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("score").description("사용자 점수").type("Number")
                        )));
    }

    @Test
    @DisplayName("퀴즈를 풀고, 정답과 오류에 해당하는 로직을 실행한다")
    void quiz_status_put() throws Exception {
        //given
        given(userQuizService.updateQuizStatus(any(Long.class), any(QuizStatusRequest.class), any(User.class))).willReturn("CORRECT");
        //when & then
        mvc.perform(put(REQUEST_URL + "/{id}/result", 1L)
                        .content(createJson(퀴즈_결과_요청))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("put-quiz-status",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        pathParameters(
                                parameterWithName("id").description("퀴즈 ID")
                        ),
                        requestFields(
                                fieldWithPath("quizStatus").description("퀴즈 상태 (CORRECT / WRONG)"),
                                fieldWithPath("type").description("오늘의 퀴즈인지, 그냥 퀴즈 풀이인지 전달 (TODAY / NORMAL)")
                        )));
    }
}