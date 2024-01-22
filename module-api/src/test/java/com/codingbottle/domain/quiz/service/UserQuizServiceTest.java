package com.codingbottle.domain.quiz.service;

import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.quiz.repo.UserQuizQueryRepository;
import com.codingbottle.exception.ApplicationErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.codingbottle.fixture.DomainFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserQuizServiceTest {
    @InjectMocks
    private UserQuizService userQuizService;

    @Mock
    private UserQuizQueryRepository userQuizQueryRepository;

    @Test
    @DisplayName("틀린 문제가 없으면 예외를 던진다")
    void if_there_is_no_wrong_answer_then_throw_exception() {
        //given
        given(userQuizQueryRepository.findRandomWrongQuizzesByUser(any())).willReturn(List.of());
        //when & then
        assertThatThrownBy(() -> userQuizService.findRandomWrongQuizzesByUser(유저1))
                .isInstanceOf(ApplicationErrorException.class);
    }

    @Test
    @DisplayName("틀린 문제 중 랜덤으로 문제를 조회한다")
    void find_random_wrong_quizzes() {
        //given
        given(userQuizQueryRepository.findRandomWrongQuizzesByUser(any())).willReturn(List.of(퀴즈1, 퀴즈2));
        //when
        List<QuizResponse> quizzes = userQuizService.findRandomWrongQuizzesByUser(유저1);
        //then
        assertThat(quizzes).isEqualTo(List.of(QuizResponse.from(퀴즈1), QuizResponse.from(퀴즈2)));
    }
}