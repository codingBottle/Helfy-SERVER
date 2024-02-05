package com.codingbottle.redis.domain.weather.model;

public record WeatherInfo(
        WeatherCode weatherCode,
        Double temp,
        Integer humidity
) {
    public static WeatherInfo of(WeatherCode weatherCode, Double temp, Integer humidity) {
        return new WeatherInfo(weatherCode, temp, humidity);
    }
}
