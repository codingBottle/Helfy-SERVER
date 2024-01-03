package com.codingbottle.domain.quiz.entity;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserQuiz extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuizStatus quizStatus;

    @Builder
    public UserQuiz(Quiz quiz, User user, QuizStatus quizStatus) {
        this.quiz = quiz;
        this.user = user;
        this.quizStatus = quizStatus;
    }

    public void updateQuizStatus(QuizStatus quizStatus){
        this.quizStatus = quizStatus;
    }
}
