package com.codingbottle.domain.weather.model;

import com.codingbottle.redis.domain.quiz.model.UserInfo;
import com.codingbottle.redis.domain.weather.model.WeatherInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "날씨 정보 응답")
public record WeatherResponse(
        @Schema(description = "사용자 정보", implementation = UserInfo.class)
        UserInfo userInfo,
        @Schema(description = "날씨 정보", implementation = WeatherInfo.class)
        WeatherInfo weatherInfo
) {
    public static WeatherResponse of(UserInfo userInfo, WeatherInfo weatherInfo) {
        return new WeatherResponse(userInfo, weatherInfo);
    }
}
