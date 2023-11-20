package com.codingbottle.domain.quiz.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.model.Type;
import com.codingbottle.domain.quiz.repo.QuizQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.codingbottle.fixture.DomainFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {
    @InjectMocks
    private QuizService quizService;

    @Mock
    private QuizQueryRepository quizRepository;

    @ParameterizedTest
    @CsvSource({"TODAY","NORMAL"})
    @DisplayName("퀴즈 타입으로 퀴즈를 조회한다")
    void findByType(Type type) {
        //given
        given(quizRepository.findRandomQuizzes(any(User.class), anyInt())).willReturn(List.of(퀴즈1, 퀴즈2));
        //when
        List<Quiz> quizzes = quizService.findByType(유저1, type);
        //then
        assertThat(quizzes.size()).isEqualTo(2);
    }
}