package com.codingbottle.common.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesRedisService {
    private final RedisTemplate<String, String> likesRedisTemplate;

    public void setLikeStatus(Long userId, Long postId, boolean likeStatus) throws JsonProcessingException {
        String mapperK = buildLikeKey(userId, postId);

        if (likeStatus) {
            likesRedisTemplate.opsForValue().set(mapperK, "LIKED");
        } else {
            likesRedisTemplate.delete(mapperK);
        }
    }

    public Boolean getLikeStatus(Long userId, Long postId) {
        String mapperK = buildLikeKey(userId, postId);
        return likesRedisTemplate.hasKey(mapperK);
    }

    private String buildLikeKey(Long userId, Long postId) {
        return "user::" + userId + "::post::" + postId;
    }
}
