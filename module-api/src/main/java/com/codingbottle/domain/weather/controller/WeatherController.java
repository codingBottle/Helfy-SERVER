package com.codingbottle.domain.weather.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.weather.model.WeatherResponse;
import com.codingbottle.domain.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "날씨", description = "날씨 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> getCurrentWeather(@AuthenticationPrincipal User user) {
        WeatherResponse weather = weatherService.getWeather(user);
        return ResponseEntity.ok(weather);
    }
}
