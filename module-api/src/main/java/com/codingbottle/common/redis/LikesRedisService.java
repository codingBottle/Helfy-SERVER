package com.codingbottle.common.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesRedisService {
    private final RedisTemplate<Long, Long> likesRedisTemplate;

    public void setLikes(Long userId, Long postId) throws JsonProcessingException {
        likesRedisTemplate.opsForHash().put(postId, userId, true);
    }

    public void deleteLikes(Long userId, Long postId) {
        likesRedisTemplate.opsForHash().delete(postId, userId);
    }

    public Boolean isAlreadyLikes(Long userId, Long postId) {
        return likesRedisTemplate.opsForHash().hasKey(postId, userId);
    }

    public Boolean deleteLikesPost(Long postId) {
        return likesRedisTemplate.delete(postId);
    }
}
