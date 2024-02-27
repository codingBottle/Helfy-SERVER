package com.codingbottle.domain.weather.controller;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.weather.model.WeatherResponse;
import com.codingbottle.domain.weather.restapi.WeatherApi;
import com.codingbottle.domain.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WeatherController implements WeatherApi {
    private final WeatherService weatherService;

    @Override
    public WeatherResponse getCurrentWeather(@AuthenticationPrincipal User user) {
        return weatherService.getWeather(user);
    }
}
