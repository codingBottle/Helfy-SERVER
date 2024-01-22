package com.codingbottle.domain.quiz.repo;

import com.codingbottle.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizSimpleJPARepository extends JpaRepository<Quiz, Long> {
}
