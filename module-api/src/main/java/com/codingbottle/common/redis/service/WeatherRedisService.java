package com.codingbottle.common.redis.service;

import com.codingbottle.domain.region.entity.Region;
import com.codingbottle.domain.weather.model.WeatherResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class WeatherRedisService {
    private final RedisTemplate<String, Object> weatherRedisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherRedisService(@Qualifier("weatherRedisTemplate") RedisTemplate<String, Object> weatherRedisTemplate) {
        this.weatherRedisTemplate = weatherRedisTemplate;
    }

    public void setObjectValues(Region region, WeatherResponse value) throws JsonProcessingException {
        String weatherStatus = objectMapper.writeValueAsString(value);
        weatherRedisTemplate.opsForValue().set(region.name(), weatherStatus);
    }

    public WeatherResponse getObjectValues(Region key) throws JsonProcessingException {
        String value = String.valueOf(weatherRedisTemplate.opsForValue().get(key.toString()));
        return objectMapper.readValue(value, WeatherResponse.class);
    }
}
