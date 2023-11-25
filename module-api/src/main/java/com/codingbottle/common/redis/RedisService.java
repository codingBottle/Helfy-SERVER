package com.codingbottle.common.redis;

import com.codingbottle.domain.region.entity.Region;
import com.codingbottle.domain.weather.model.WeatherResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setObjectValues(Region key, WeatherResponse value) throws JsonProcessingException {
        String mapperK = writeKeyAsString(key);
        var mapperV = objectMapper.writeValueAsString(value);

        redisTemplate.opsForValue().set(mapperK, mapperV);
    }

    public WeatherResponse getObjectValues(Region key) throws JsonProcessingException {
        String mapperK = writeKeyAsString(key);
        String value = redisTemplate.opsForValue().get(mapperK);

        return objectMapper.readValue(value, WeatherResponse.class);
    }

    private String writeKeyAsString(Region key) throws JsonProcessingException {
        return objectMapper.writeValueAsString(key);
    }

    public void setLikeStatus(Long userId, Long postId, boolean likeStatus) throws JsonProcessingException {
        String mapperK = buildLikeKey(userId, postId);
        var mapperV = objectMapper.writeValueAsString(likeStatus);

        redisTemplate.opsForValue().set(mapperK, mapperV);
    }

    public Boolean getLikeStatus(Long userId, Long postId) throws JsonProcessingException {
        String mapperK = buildLikeKey(userId, postId);
        String value = redisTemplate.opsForValue().get(mapperK);

        return objectMapper.readValue(value, Boolean.class);
    }

    private String buildLikeKey(Long userId, Long postId) {
        return "user::" + userId + "::post::" + postId;
    }
}
