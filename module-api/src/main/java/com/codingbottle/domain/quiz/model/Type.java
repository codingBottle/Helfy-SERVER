package com.codingbottle.domain.quiz.model;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.repo.QuizQueryRepository;

import java.util.List;

public enum Type {
    TODAY{
        @Override
        public List<Quiz> getQuizzes(User user, QuizQueryRepository quizRepository) {
            return quizRepository.findRandomQuizzes(user, 3);
        }
    },
    NORMAL{
        @Override
        public List<Quiz> getQuizzes(User user, QuizQueryRepository quizRepository) {
            return quizRepository.findRandomWrongQuizzes(user, 10);
        }
    };

    public abstract List<Quiz> getQuizzes(User user, QuizQueryRepository quizRepository);
}
