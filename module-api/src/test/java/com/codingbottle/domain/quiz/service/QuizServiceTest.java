package com.codingbottle.domain.quiz.service;

import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.quiz.model.Type;
import com.codingbottle.domain.quiz.repo.QuizQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.codingbottle.fixture.DomainFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {
    @InjectMocks
    private QuizService quizService;

    @Mock
    private QuizQueryRepository quizRepository;

    @Test
    @DisplayName("일반 퀴즈를 조회한다")
    void findByNormalQuiz() {
        //given
        given(quizRepository.findRandomWrongQuizzes(any(User.class), anyInt())).willReturn(List.of(퀴즈1, 퀴즈2));
        //when
        List<QuizResponse> quizzes = quizService.findByType(유저1, Type.NORMAL);
        //then
        assertThat(quizzes.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("오늘의 퀴즈를 조회한다")
    void findByTodayQuiz(){
        //given
        given(quizRepository.findRandomQuizzes(any(User.class), anyInt())).willReturn(List.of(퀴즈1, 퀴즈2));
        //when
        List<QuizResponse> quizzes = quizService.findByType(유저1, Type.TODAY);
        //then
        assertThat(quizzes.size()).isEqualTo(2);
    }
}