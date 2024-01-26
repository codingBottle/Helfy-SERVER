package com.codingbottle.redis.domain.quiz.model;

public record QuizRankUserData(
        Long id,
        String nickname
) {
    public static QuizRankUserData of(Long userId, String nickname) {
        return new QuizRankUserData(userId, nickname);
    }
}
