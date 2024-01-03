package com.codingbottle.domain.quiz.repo;

import com.codingbottle.domain.quiz.entity.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserQuizSimpleJPARepository extends JpaRepository<UserQuiz, Long> {
    Boolean existsByUserIdAndQuizId(Long userId, Long quizId);

    Optional<UserQuiz> findByUserIdAndQuizId(Long userId, Long quizId);
}
