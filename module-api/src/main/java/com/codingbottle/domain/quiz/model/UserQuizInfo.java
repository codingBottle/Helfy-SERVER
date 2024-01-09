package com.codingbottle.domain.quiz.model;

public record UserQuizInfo(
        String nickname,
        int score
) {
    public static UserQuizInfo from(String nickname, int score) {
        return new UserQuizInfo(nickname, score);
    }
}
