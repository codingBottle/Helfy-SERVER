package com.codingbottle.domain.weather.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.redis.WeatherRedisService;
import com.codingbottle.domain.region.entity.Region;
import com.codingbottle.domain.weather.entity.WeatherCode;
import com.codingbottle.domain.weather.model.WeatherResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {
    @InjectMocks
    WeatherService weatherService;

    @Mock
    WeatherRedisService redisService;

    @Test
    @DisplayName("해당 지역 날씨 정보 조회")
    void get_weather() throws JsonProcessingException {
        // given
        WeatherResponse mockWeatherResponse = new WeatherResponse(WeatherCode.ATMOSPHERE, 20.0, 10);

        given(redisService.getObjectValues(any(Region.class))).willReturn(mockWeatherResponse);

        User user = User.builder()
                .region(Region.SEOUL)
                .build();
        // when
        WeatherResponse weatherResponse = weatherService.getWeather(user);
        // then
        assertThat(weatherResponse).isEqualTo(mockWeatherResponse);
    }
}