package com.codingbottle.domain.quiz.repo;

import com.codingbottle.auth.entity.User;
import com.codingbottle.auth.repository.UserRepository;
import com.codingbottle.common.annotation.RepositoryTest;
import com.codingbottle.domain.quiz.entity.Quiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.codingbottle.fixture.CoreDomainFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class UserQuizQueryRepositoryTest {
    @Autowired
    private UserQuizQueryRepository userQuizQueryRepository;

    @Autowired
    private UserQuizSimpleJPARepository userQuizSimpleJPARepository;

    @Autowired
    private QuizSimpleJPARepository quizSimpleJPARepository;

    @Autowired
    private UserRepository userRepository;

    private User 유저;

    @BeforeEach
    void setUp() {
        유저 = userRepository.save(유저1);
        quizSimpleJPARepository.saveAll(List.of(퀴즈1, 퀴즈2));
        userQuizSimpleJPARepository.saveAll(List.of(유저1_오답1, 유저1_오답2));
    }

    @Test
    @DisplayName("유저가 틀린 문제를 조회한다.")
    void find_user_wrong_quizzes() {
        //when
        List<Quiz> randomWrongQuizzesByUser = userQuizQueryRepository.findRandomWrongQuizzesByUser(유저);
        //then
        assertThat(randomWrongQuizzesByUser).contains(퀴즈1, 퀴즈2);
    }
}