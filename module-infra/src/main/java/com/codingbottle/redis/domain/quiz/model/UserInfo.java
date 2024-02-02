package com.codingbottle.redis.domain.quiz.model;

public record UserInfo(
        Long id,
        String nickname
) {
    public static UserInfo of(Long userId, String nickname) {
        return new UserInfo(userId, nickname);
    }
}
