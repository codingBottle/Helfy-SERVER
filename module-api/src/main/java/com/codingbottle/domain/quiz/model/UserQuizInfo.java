package com.codingbottle.domain.quiz.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 퀴즈 점수 정보")
public record UserQuizInfo(
        @Schema(description = "닉네임", example = "김코딩")
        String nickname,
        @Schema(description = "점수", example = "100")
        int score
) {
    public static UserQuizInfo from(String nickname, int score) {
        return new UserQuizInfo(nickname, score);
    }
}
