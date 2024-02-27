package com.codingbottle.domain.quiz.model;

import com.codingbottle.domain.quiz.entity.QuizStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "퀴즈 상태 변경 요청")
public record QuizStatusRequest(
        @NotBlank
        @Schema(description = "퀴즈 정답시 CORRECT, 오답시 WRONG", example = "CORRECT")
        QuizStatus quizStatus
) {
}
