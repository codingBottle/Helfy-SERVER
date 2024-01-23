package com.codingbottle.domain.quiz.service;

import com.codingbottle.domain.quiz.model.QuizResponse;
import com.codingbottle.domain.quiz.model.UserQuizInfo;
import com.codingbottle.domain.quiz.repo.UserQuizQueryRepository;
import com.codingbottle.domain.quiz.repo.UserQuizSimpleJPARepository;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.redis.service.QuizRankRedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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

    @Test
    @DisplayName("퀴즈 최초 풀이 시 정답일 경우 정답 처리 후 점수를 획득한다")
    void if_quiz_is_correct_then_update_quiz_status_and_get_10_points() {
        //given
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
        given(userQuizSimpleJPARepository.existsByUserIdAndQuizId(any(), any())).willReturn(true);

        given(userQuizSimpleJPARepository.findByUserIdAndQuizId(any(), any())).willReturn(Optional.ofNullable(퀴즈_유저_오답1));

        doNothing().when(quizRankRedisService).updateScore(any(), any(Integer.class));
        //when
        String status = userQuizService.updateQuizStatus(퀴즈1.getId(), 퀴즈_정답_요청, 유저1);
        //then
        assertThat(status).isEqualTo("CORRECT");
    }

    @Test
    @DisplayName("틀린 퀴즈가 없으면 예외를 던진다")
    void if_there_is_no_wrong_answer_then_throw_exception() {
        //given
        given(userQuizQueryRepository.findRandomWrongQuizzesByUser(any())).willReturn(List.of());
        //when & then
        assertThatThrownBy(() -> userQuizService.findRandomWrongQuizzesByUser(유저1))
                .isInstanceOf(ApplicationErrorException.class);
    }

    @Test
    @DisplayName("틀린 퀴즈 중 랜덤으로 퀴즈를 조회한다")
    void find_random_wrong_quizzes() {
        //given
        given(userQuizQueryRepository.findRandomWrongQuizzesByUser(any())).willReturn(List.of(퀴즈1, 퀴즈2));
        //when
        List<QuizResponse> quizzes = userQuizService.findRandomWrongQuizzesByUser(유저1);
        //then
        assertThat(quizzes).isEqualTo(List.of(QuizResponse.from(퀴즈1), QuizResponse.from(퀴즈2)));
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
}