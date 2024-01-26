package com.codingbottle.domain.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CurrentWeatherRequest(
        @JsonProperty("weather") List<Weather> weather,
        @JsonProperty("main") WeatherMain main
) {
    public record WeatherMain(
            @JsonProperty("temp")
            Double temp,
            @JsonProperty("humidity")
            Integer humidity
    ) {
    }

    public record Weather(
            @JsonProperty("id")
            String id
    ) {
    }
}

