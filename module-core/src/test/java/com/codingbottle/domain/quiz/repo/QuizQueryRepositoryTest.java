package com.codingbottle.domain.quiz.repo;

import com.codingbottle.auth.entity.User;
import com.codingbottle.auth.repository.UserRepository;
import com.codingbottle.common.annotation.RepositoryTest;
import com.codingbottle.domain.quiz.entity.Quiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.codingbottle.fixture.CoreDomainFixture.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RepositoryTest
class QuizQueryRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizSimpleJPARepository quizSimpleJPARepository;

    @Autowired
    private UserQuizSimpleJPARepository userQuizSimpleJPARepository;

    @Autowired
    private QuizQueryRepository quizQueryRepository;


    User 유저;

    @BeforeEach
    void setUp() {
        유저 = userRepository.save(유저1);
        quizSimpleJPARepository.saveAll(List.of(퀴즈1, 퀴즈2));
    }

    @Test
    @DisplayName("랜덤으로 퀴즈를 조회한다")
    void find_random_quizzes() {
        //when
        List<Quiz> randomQuizzes = quizQueryRepository.findRandomQuizzes(유저, 3);
        //then
        assertThat(randomQuizzes).hasSize(2);
    }

    @Test
    @DisplayName("랜덤으로 퀴즈를 조회하는데 이미 사용자가 푼 문제는 제외한다.")
    void find_random_quizzes_exclude_user_quiz() {
        //given
        userQuizSimpleJPARepository.save(유저1_퀴즈1);
        //when
        List<Quiz> randomQuizzes = quizQueryRepository.findRandomQuizzes(유저, 3);
        //then
        assertThat(randomQuizzes).hasSize(1);
    }

    @ParameterizedTest
    @CsvSource({"1, 1", "2, 2"})
    @DisplayName("랜덤으로 퀴즈를 조회하는데 limit을 지정할 수 있다.")
    void find_random_quizzes_limit(int limit, int expectedSize) {
        //when
        List<Quiz> randomQuizzes = quizQueryRepository.findRandomQuizzes(유저, limit);
        //then
        assertThat(randomQuizzes).hasSize(expectedSize);
    }
}
