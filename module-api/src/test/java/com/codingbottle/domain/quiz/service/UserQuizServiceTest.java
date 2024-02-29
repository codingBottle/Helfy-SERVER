package com.codingbottle.domain.quiz.service;

import com.codingbottle.domain.quiz.model.UserQuizInfo;
import com.codingbottle.domain.quiz.repo.UserQuizQueryRepository;
import com.codingbottle.domain.quiz.repo.UserQuizSimpleJPARepository;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.redis.domain.quiz.service.QuizRankRedisService;
import com.codingbottle.redis.domain.quiz.service.TodayQuizRedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.codingbottle.fixture.DomainFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class UserQuizServiceTest {
    @InjectMocks
    private UserQuizService userQuizService;

    @Mock
    private UserQuizQueryRepository userQuizQueryRepository;

    @Mock
    private UserQuizSimpleJPARepository userQuizSimpleJPARepository;

    @Mock
    private QuizService quizService;

    @Mock
    private QuizRankRedisService quizRankRedisService;

    @Mock
    private TodayQuizRedisService todayQuizRedisService;

    @Test
    @DisplayName("퀴즈 최초 풀이 시 정답일 경우 정답 처리 후 점수를 획득한다")
    void if_quiz_is_correct_then_update_quiz_status_and_get_10_points() {
        //given
        given(todayQuizRedisService.isAlreadySolved(any())).willReturn(false);

        given(userQuizSimpleJPARepository.existsByUserIdAndQuizId(any(), any())).willReturn(false);

        given(userQuizSimpleJPARepository.save(any())).willReturn(퀴즈_유저_정답1);

        given(quizService.findById(any())).willReturn(퀴즈1);

        doNothing().when(quizRankRedisService).updateScore(any(), any(Integer.class));
        //when
        String status = userQuizService.updateQuizStatus(퀴즈1.getId(), 퀴즈_정답_요청, 유저1);
        //then
        assertThat(status).isEqualTo("CORRECT");
    }

    @Test
    @DisplayName("퀴즈 최초 풀이 시 오답일 경우 오답 처리 후 점수를 획득하지 않는다")
    void if_quiz_is_wrong_then_update_quiz_status_and_get_0_points() {
        //given
        given(todayQuizRedisService.isAlreadySolved(any())).willReturn(false);

        given(userQuizSimpleJPARepository.existsByUserIdAndQuizId(any(), any())).willReturn(false);

        given(userQuizSimpleJPARepository.save(any())).willReturn(퀴즈_유저_오답1);

        given(quizService.findById(any())).willReturn(퀴즈1);
        //when
        String status = userQuizService.updateQuizStatus(퀴즈1.getId(), 퀴즈_오답_요청, 유저1);
        //then
        assertThat(status).isEqualTo("WRONG");
    }

    @Test
    @DisplayName("이미 정답을 맞춘 퀴즈에 대해 정답을 맞추면 예외를 던진다")
    void if_quiz_is_already_correct_then_throw_exception() {
        //given
        given(todayQuizRedisService.isAlreadySolved(any())).willReturn(false);

        given(userQuizSimpleJPARepository.existsByUserIdAndQuizId(any(), any())).willReturn(true);

        given(userQuizSimpleJPARepository.findByUserIdAndQuizId(any(), any())).willReturn(Optional.ofNullable(퀴즈_유저_정답1));
        //when & then
        assertThatThrownBy(() -> userQuizService.updateQuizStatus(퀴즈1.getId(), 퀴즈_정답_요청, 유저1))
                .isInstanceOf(ApplicationErrorException.class);
    }

    @Test
    @DisplayName("오답 처리시 이미 오답인 문제는 WRONG 상태를 유지한다")
    void if_quiz_is_already_wrong_then_keep_wrong_status() {
        //given
        given(todayQuizRedisService.isAlreadySolved(any())).willReturn(true);

        given(userQuizSimpleJPARepository.existsByUserIdAndQuizId(any(), any())).willReturn(true);
        //when
        String status = userQuizService.updateQuizStatus(퀴즈1.getId(), 퀴즈_오답_요청, 유저1);
        //then
        assertThat(status).isEqualTo("WRONG");
    }

    @Test
    @DisplayName("오답 처리된 퀴즈를 맞추게 되면 해당 퀴즈를 정답 처리하고 점수를 획득한다")
    void if_quiz_is_wrong_then_update_quiz_status_and_get_10_points() {
        //given
        given(todayQuizRedisService.isAlreadySolved(any())).willReturn(true);

        given(userQuizSimpleJPARepository.existsByUserIdAndQuizId(any(), any())).willReturn(true);

        given(userQuizSimpleJPARepository.findByUserIdAndQuizId(any(), any())).willReturn(Optional.ofNullable(퀴즈_유저_오답1));

        doNothing().when(quizRankRedisService).updateScore(any(), any(Integer.class));
        //when
        String status = userQuizService.updateQuizStatus(퀴즈1.getId(), 퀴즈_정답_요청, 유저1);
        //then
        assertThat(status).isEqualTo("CORRECT");
    }

    @Test
    @DisplayName("사용자의 퀴즈 점수를 조회한다")
    void find_user_quiz_score() {
        //given
        given(quizRankRedisService.getScore(any())).willReturn(10);
        //when
        UserQuizInfo userQuizInfo = userQuizService.getUserQuizInfo(유저1);
        //then
        assertThat(userQuizInfo.score()).isEqualTo(10);
    }

    @Test
    @DisplayName("사용자가 처음 오늘의 퀴즈를 풀면 오늘의 퀴즈를 풀었음을 저장한다")
    void if_user_solves_today_quiz_first_time_then_save_it() {
        //given
        given(todayQuizRedisService.isAlreadySolved(any())).willReturn(false);

        given(userQuizSimpleJPARepository.existsByUserIdAndQuizId(any(), any())).willReturn(false);

        given(userQuizSimpleJPARepository.save(any())).willReturn(퀴즈_유저_정답1);

        given(quizService.findById(any())).willReturn(퀴즈1);

        doNothing().when(todayQuizRedisService).setSolved(any());

        doNothing().when(quizRankRedisService).updateScore(any(), any(Integer.class));
        //when
        String status = userQuizService.updateQuizStatus(퀴즈1.getId(), 퀴즈_정답_요청, 유저1);
        //then
        assertThat(status).isEqualTo("CORRECT");
    }
}