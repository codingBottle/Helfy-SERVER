package com.codingbottle.domain.quiz.repo;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.entity.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserQuizSimpleJPARepository extends JpaRepository<UserQuiz, Long> {
    Optional<UserQuiz> findByUserAndQuiz(User user, Quiz quiz);
}
