package com.codingbottle.domain.quiz.service;

import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.quiz.model.Type;
import com.codingbottle.domain.quiz.repo.QuizQueryRepository;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.redis.domain.quiz.service.TodayQuizRedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.codingbottle.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Mock
    private TodayQuizRedisService todayQuizRedisService;

    @Test
    @DisplayName("일반 퀴즈를 조회한다")
    void findByNormalQuiz() {
        //given
        given(quizRepository.findRandomQuizzes(any(User.class), anyInt())).willReturn(List.of(퀴즈1, 퀴즈2));
        //when
        List<QuizResponse> quizzes = quizService.findByType(유저1, Type.NORMAL);
        //then
        assertThat(quizzes.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("오늘의 퀴즈를 조회한다")
    void findByTodayQuiz(){
        //given
        given(quizRepository.findRandomTodayQuizzes(any(User.class), anyInt())).willReturn(List.of(퀴즈1, 퀴즈2));
        //when
        List<QuizResponse> quizzes = quizService.findByType(유저1, Type.TODAY);
        //then
        assertThat(quizzes.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("오늘의 퀴즈를 이미 푼 사용자는 오늘의 퀴즈를 조회할 수 없다")
    void findByTodayQuizAndAlreadySolved(){
        //given
        given(todayQuizRedisService.isAlreadySolved(any())).willReturn(true);
        //when & then
        assertThatThrownBy(() -> quizService.findByType(유저1, Type.TODAY))
                .isInstanceOf(ApplicationErrorException.class);
    }

    @Test
    @DisplayName("퀴즈 조회시 퀴즈가 없으면 빈 리스트를 반환한다")
    void findByTypeAndEmptyQuiz(){
        //given
        given(quizRepository.findRandomQuizzes(any(User.class), anyInt())).willReturn(List.of());
        //when
        List<QuizResponse> quizzes = quizService.findByType(유저1, Type.NORMAL);
        //then
        assertThat(quizzes.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("오답 퀴즈를 조회한다")
    void findByWrongQuiz(){
        //given
        given(quizRepository.findRandomWrongQuizzes(any(User.class), anyInt())).willReturn(List.of(퀴즈1, 퀴즈2));
        //when
        List<QuizResponse> quizzes = quizService.findByType(유저1, Type.WRONG);
        //then
        assertThat(quizzes.size()).isEqualTo(2);
    }
}