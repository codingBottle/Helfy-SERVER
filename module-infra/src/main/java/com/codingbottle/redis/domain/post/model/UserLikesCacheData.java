package com.codingbottle.redis.domain.post.model;


public record UserLikesCacheData(
        Long id,
        boolean status
) {
    public static UserLikesCacheData of(Long userId, boolean status) {
        return new UserLikesCacheData(userId, status);
    }
}
