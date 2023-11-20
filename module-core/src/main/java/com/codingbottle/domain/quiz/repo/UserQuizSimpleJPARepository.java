package com.codingbottle.domain.quiz.repo;

import com.codingbottle.domain.quiz.entity.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizSimpleJPARepository extends JpaRepository<UserQuiz, Long> {
}
