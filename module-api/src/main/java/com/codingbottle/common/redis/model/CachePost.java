package com.codingbottle.common.redis.model;

import com.codingbottle.domain.post.entity.Post;

public record CachePost(
        Long id,
        CacheUser user
) {
    public static CachePost from(Post post) {
        return new CachePost(post.getId(), CacheUser.from(post.getUser()));
    }
}
