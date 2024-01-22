package com.codingbottle.redis.service;

import com.codingbottle.redis.model.CachePost;
import com.codingbottle.redis.model.CacheUser;
import com.codingbottle.domain.post.entity.Post;
import com.codingbottle.domain.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesRedisService {
    private final RedisTemplate<Object, Object> likesRedisTemplate;

    public void setLikes(User user, Post post) throws JsonProcessingException {
        likesRedisTemplate.opsForSet().add(CachePost.from(post), CacheUser.from(user));
    }

    public void deleteLikes(User user, Post post) {
        likesRedisTemplate.opsForSet().remove(CachePost.from(post), CacheUser.from(user));
    }

    public Boolean isAlreadyLikes(User user, Post post) {
        return likesRedisTemplate.opsForSet().isMember(CachePost.from(post), CacheUser.from(user));
    }

    public Boolean deleteLikesPost(Post post) {
        return likesRedisTemplate.delete(CachePost.from(post));
    }
}
