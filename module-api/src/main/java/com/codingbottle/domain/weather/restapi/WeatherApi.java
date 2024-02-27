package com.codingbottle.domain.weather.restapi;

import com.codingbottle.common.annotation.DefaultApiResponse;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.weather.model.WeatherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "날씨", description = "날씨 API")
@DefaultApiResponse
@RestController
@RequestMapping("/api/v1/weather")
public interface WeatherApi {
    @Operation(summary = "날씨 조회", description = "날씨를 조회합니다.")
    @GetMapping
    WeatherResponse getCurrentWeather(User user);
}
