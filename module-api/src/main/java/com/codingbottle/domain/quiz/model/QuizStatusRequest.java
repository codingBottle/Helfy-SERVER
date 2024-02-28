package com.codingbottle.domain.quiz.model;

import com.codingbottle.domain.quiz.entity.QuizStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "퀴즈 상태 변경 요청")
public record QuizStatusRequest(
        @NotBlank
        @Schema(description = "퀴즈 정답시 CORRECT, 오답시 WRONG", example = "CORRECT")
        QuizStatus quizStatus,
        @NotNull
        @Schema(description = "Type을 보내야 해당 퀴즈의 점수를 차등 부여할 수 있기 때문에 Type을 보내주세요", example = "TODAY")
        Type type
) {
}
