package com.codingbottle.common.redis.service;

import com.codingbottle.domain.region.entity.Region;
import com.codingbottle.domain.weather.model.WeatherResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherRedisService {
    private final RedisTemplate<String, String> weatherRedisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setObjectValues(Region key, WeatherResponse value) throws JsonProcessingException {
        String mapperK = writeKeyAsString(key);
        var mapperV = objectMapper.writeValueAsString(value);

        weatherRedisTemplate.opsForValue().set(mapperK, mapperV);
    }

    public WeatherResponse getObjectValues(Region key) throws JsonProcessingException {
        String mapperK = writeKeyAsString(key);
        String value = weatherRedisTemplate.opsForValue().get(mapperK);

        return objectMapper.readValue(value, WeatherResponse.class);
    }

    private String writeKeyAsString(Region key) throws JsonProcessingException {
        return objectMapper.writeValueAsString(key);
    }
}
