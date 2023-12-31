package com.codingbottle.domain.quiz.controller;

import com.codingbottle.auth.entity.User;
import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.domain.quiz.model.QuizStatusRequest;
import com.codingbottle.domain.quiz.model.Type;
import com.codingbottle.domain.quiz.service.QuizService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("QuizController 테스트")
@ContextConfiguration(classes = QuizController.class)
@WebMvcTest(value = QuizController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class QuizControllerTest extends RestDocsTest {
    @MockBean
    private QuizService quizService;
    @MockBean
    private UserQuizService userQuizService;

    private static final String REQUEST_URL = "/api/v1/quiz";

    @Test
    @DisplayName("오늘의 퀴즈를 조회한다")
    void get_today_quiz() throws Exception {
        //given
        given(quizService.findByType(any(User.class), any(Type.class))).willReturn(List.of(퀴즈1, 퀴즈2));
        //when & then
        mvc.perform(get(REQUEST_URL)
                        .queryParam("type", "TODAY")
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("quiz-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader(),
                        queryParameters(
                                parameterWithName("type").description("오늘의 퀴즈 (TODAY) / 퀴즈 풀어보기 (NORMAL)")
                        ),
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
                                fieldWithPath("quizStatus").description("퀴즈 상태 (CORRECT / WRONG)")
                        )));
    }

}