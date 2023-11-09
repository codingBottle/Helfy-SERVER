package com.codingbottle.domain.weather.entity;

import java.util.Arrays;

public enum WeatherCode {
    THUNDERSTORM("2"),
    DRIZZLE("3"),
    RAIN("5"),
    SNOW("6"),
    ATMOSPHERE("7"),
    CLEAR("8"),
    CLOUDS("9");

    private final String code;
    WeatherCode(String code) {
        this.code = code;
    }

    public static WeatherCode of(String id) {
        if(isClouds(id)){
            id = "900";
        }

        String code = id.substring(0, 1);
        return Arrays.stream(values())
                .filter(weatherCode -> weatherCode.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 날씨 코드가 없습니다."));
    }

    private static boolean isClouds(String id) {
        return id.startsWith("801") || id.startsWith("802") || id.startsWith("803") || id.startsWith("804");
    }
}
