package com.codingbottle.redis.domain.weather.model;

public record WeatherResponse(
        WeatherCode weatherCode,
        Double temp,
        Integer humidity
) {
    public static WeatherResponse of(WeatherCode weatherCode, Double temp, Integer humidity) {
        return new WeatherResponse(weatherCode, temp, humidity);
    }
}
