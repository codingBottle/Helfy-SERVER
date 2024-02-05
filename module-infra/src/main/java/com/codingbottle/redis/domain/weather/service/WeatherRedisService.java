package com.codingbottle.redis.domain.weather.service;

import com.codingbottle.redis.domain.weather.model.WeatherInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherRedisService {
    private final RedisTemplate<String, Object> weatherRedisTemplate;



    public void setWeatherData(String region, WeatherInfo weather) throws JsonProcessingException {
        weatherRedisTemplate.opsForValue().set(region, weather);
    }

    public WeatherInfo getWeatherData(String region) throws JsonProcessingException {
        return (WeatherInfo) weatherRedisTemplate.opsForValue().get(region);
    }
}
