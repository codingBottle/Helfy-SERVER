package com.codingbottle.domain.quiz.model;

import com.codingbottle.domain.image.model.ImageResponse;
import com.codingbottle.domain.quiz.entity.Quiz;
import com.codingbottle.domain.quiz.entity.QuizType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

@Schema(description = "퀴즈 정보 응답 데이터 모델")
public record QuizResponse(
        @Schema(description = "퀴즈 ID", example = "1")
        Long id,
        @Schema(description = "퀴즈 문제", example = "다음 중 고양이의 영어 이름은?")
        String question,
        @Schema(description = "퀴즈 정답", example = "cat")
        String answer,
        @Schema(description = "퀴즈 보기 목록", example = "{\"1\":\"dog\",\"2\":\"cat\",\"3\":\"rabbit\",\"4\":\"lion\"}")
        Map<String, String> choices,
        @Schema(description = "퀴즈 이미지 정보")
        ImageResponse image,
        @Schema(description = "퀴즈 타입", example = "TODAY")
        QuizType quizType) {
    public static QuizResponse from(Quiz quiz) {
        return new QuizResponse(quiz.getId(),quiz.getQuestion(), quiz.getAnswer(), new HashMap<>(quiz.getChoices()), quiz.getImage() == null ? null : ImageResponse.from(quiz.getImage()), quiz.getQuizType());
    }
}
