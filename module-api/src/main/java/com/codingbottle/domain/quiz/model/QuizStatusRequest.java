package com.codingbottle.domain.quiz.model;

import com.codingbottle.domain.quiz.entity.QuizStatus;
import jakarta.validation.constraints.NotBlank;

public record QuizStatusRequest(
        @NotBlank
        QuizStatus quizStatus
) {
}
