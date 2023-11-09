package com.codingbottle.domain.weather.model;

import com.codingbottle.domain.weather.entity.WeatherCode;

public record WeatherResponse(
        WeatherCode weatherCode,
        Double temp,
        Integer humidity
) {
    public static WeatherResponse of(CurrentWeatherRequest currentWeatherRequest) {
        return new WeatherResponse(
                WeatherCode.of(currentWeatherRequest.weather().get(0).id()),
                currentWeatherRequest.main().temp(),
                currentWeatherRequest.main().humidity()
        );
    }
}
