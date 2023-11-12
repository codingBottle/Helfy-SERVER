package com.codingbottle.domain.weather.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.exception.ApplicationErrorType;
import com.codingbottle.common.redis.RedisService;
import com.codingbottle.domain.region.entity.Region;
import com.codingbottle.domain.weather.model.CurrentWeatherRequest;
import com.codingbottle.domain.weather.model.WeatherResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WebClient webClient;
    private final RedisService redisService;

    @Value("${openweathermap.api-key}")
    private String weatherApiKey;

    public WeatherResponse getWeather(User user) {
        try {
            return redisService.getObjectValues(user.getRegion());
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR, "날씨 정보를 가져오는데 실패했습니다.");
        }
    }

    @Scheduled(cron = "0 0 0/1 * * *")
    public void getAllRegionWeather() {
        Arrays.stream(Region.values())
                .forEach(this::setAllRegionWeather);

        log.info("Updated all region weather.");
    }

    private void setAllRegionWeather(Region region) {
        try {
            redisService.setObjectValues(region, getWeather(region));
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR, "날씨 정보를 가져오는데 실패했습니다.");
        }
    }

    private WeatherResponse getWeather(Region region) {
        CurrentWeatherRequest currentWeatherRequest = getRequestCurrentWeather(region);

        if (currentWeatherRequest == null) {
            throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR, "날씨 정보를 가져오는데 실패했습니다.");
        }

        return WeatherResponse.of(currentWeatherRequest);
    }

    private CurrentWeatherRequest getRequestCurrentWeather(Region region) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", region.getLatitude())
                        .queryParam("lon", region.getLongitude())
                        .queryParam("appid", weatherApiKey)
                        .queryParam("units", "metric")
                        .queryParam("lang", "kr")
                        .build())
                .retrieve()
                .bodyToMono(CurrentWeatherRequest.class)
                .block();
    }

    @PostConstruct
    private void initAllRegionWeather() {
        getAllRegionWeather();
    }
}

