package com.codingbottle.domain.quiz.model;

import com.codingbottle.domain.image.model.ImageResponse;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.entity.QuizType;

import java.util.Map;

public record QuizResponse(
        Long id,
        String question,
        String answer,
        Map<String, String> choices,
        ImageResponse image,
        QuizType quizType) {
    public static QuizResponse from(Quiz quiz) {
        return new QuizResponse(quiz.getId(),quiz.getQuestion(), quiz.getAnswer(), quiz.getChoices(), quiz.getImage() == null ? null : ImageResponse.from(quiz.getImage()), quiz.getQuizType());
    }
}
