package com.codingbottle.domain.quiz.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.model.Type;
import com.codingbottle.domain.quiz.repo.QuizQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {
    private final QuizQueryRepository quizRepository;

    public List<Quiz> findByType(User user, Type type) {
        return type.getQuizzes(user, quizRepository);
    }
}
