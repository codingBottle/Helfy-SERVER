package com.codingbottle.domain.quiz.repo;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.quiz.entity.QUserQuiz;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.entity.QuizStatus;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQuizQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QUserQuiz qUserQuiz = QUserQuiz.userQuiz;
    public List<Quiz> findRandomWrongQuizzesByUser(User user) {
        return jpaQueryFactory.select(qUserQuiz.quiz)
                .from(qUserQuiz)
                .where(qUserQuiz.user.eq(user)
                        .and(qUserQuiz.quizStatus.eq(QuizStatus.WRONG)))
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(5)
                .fetch();
    }
}

