package com.codingbottle.domain.quiz.model;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.repo.QuizQueryRepository;
import lombok.Getter;

import java.util.List;

@Getter
public enum Type {
    TODAY(20){
        @Override
        public List<Quiz> getQuizzes(User user, QuizQueryRepository quizRepository) {
            return quizRepository.findRandomTodayQuizzes(user, 3);
        }
    },
    NORMAL(10){
        @Override
        public List<Quiz> getQuizzes(User user, QuizQueryRepository quizRepository) {
            return quizRepository.findRandomQuizzes(user, 10);
        }
    },
    WRONG(5){
        @Override
        public List<Quiz> getQuizzes(User user, QuizQueryRepository quizRepository) {
            return quizRepository.findRandomWrongQuizzes(user, 10);
        }
    }
    ;

    final int score;

    Type(int score) {
        this.score = score;
    }

    public abstract List<Quiz> getQuizzes(User user, QuizQueryRepository quizRepository);
}
