package com.codingbottle.redis.domain.post.model;


public record PostCacheData(
        Long id
) {
    public static PostCacheData from(Long id) {
        return new PostCacheData(id);
    }
}

