package com.codingbottle.domain.weather.model;

import com.codingbottle.redis.domain.quiz.model.UserInfo;
import com.codingbottle.redis.domain.weather.model.WeatherInfo;

public record WeatherResponse(
        UserInfo userInfo,
        WeatherInfo weatherInfo
) {
    public static WeatherResponse of(UserInfo userInfo, WeatherInfo weatherInfo) {
        return new WeatherResponse(userInfo, weatherInfo);
    }
}
