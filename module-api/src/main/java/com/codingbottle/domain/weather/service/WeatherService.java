package com.codingbottle.domain.weather.service;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.redis.domain.weather.service.WeatherRedisService;
import com.codingbottle.redis.domain.weather.model.WeatherResponse;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.exception.ApplicationErrorType;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRedisService weatherRedisService;

    public WeatherResponse getWeather(User user) {
        try {
            return weatherRedisService.getWeatherData(user.getRegion().name());
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR, "날씨 정보를 가져오는데 실패했습니다.");
        }
    }
}

