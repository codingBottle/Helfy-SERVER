package com.codingbottle.domain.quiz.entity;

import com.codingbottle.domain.image.entity.Image;
import com.google.common.base.Objects;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quiz_question", columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(name = "quiz_answer", columnDefinition = "TEXT", nullable = false)
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(name = "quiz_type", nullable = false)
    private QuizType quizType;

    @ElementCollection
    @CollectionTable(name = "quiz_choices", joinColumns = @JoinColumn(name = "quiz_id"))
    @MapKeyColumn(name = "choice_number")
    @Column(name = "choice", columnDefinition = "TEXT", nullable = false)
    private Map<String, String> choices;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @Builder
    public Quiz(String question, String answer, QuizType quizType, Map<String, String> choices, Image image) {
        this.question = question;
        this.answer = answer;
        this.quizType = quizType;
        this.choices = choices;
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return Objects.equal(id, quiz.id)
                && Objects.equal(question, quiz.question)
                && Objects.equal(answer, quiz.answer)
                && quizType == quiz.quizType
                && Objects.equal(choices, quiz.choices)
                && Objects.equal(image, quiz.image);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, question, answer, quizType, choices, image);
    }
}
