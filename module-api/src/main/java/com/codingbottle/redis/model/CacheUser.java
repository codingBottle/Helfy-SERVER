package com.codingbottle.redis.model;

import com.codingbottle.domain.user.entity.User;

public record CacheUser(
        Long id,
        String nickname
) {
    public static CacheUser from(User user) {
        return new CacheUser(user.getId(), user.getNickname());
    }

    public static CacheUser from(Long id, String nickname) {
        return new CacheUser(id, nickname);
    }
}
