package com.codingbottle.common.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesRedisService {
    private final RedisTemplate<Long, Long> likesRedisTemplate;

    public void setLikes(Long userId, Long postId) throws JsonProcessingException {
        likesRedisTemplate.opsForSet().add(postId, userId);
    }

    public void deleteLikes(Long userId, Long postId) {
        likesRedisTemplate.opsForSet().remove(postId, userId);
    }

    public Boolean isAlreadyLikes(Long userId, Long postId) {
        return likesRedisTemplate.opsForSet().isMember(postId, userId);
    }

    public Boolean deleteLikesPost(Long postId) {
        return likesRedisTemplate.delete(postId);
    }
}
