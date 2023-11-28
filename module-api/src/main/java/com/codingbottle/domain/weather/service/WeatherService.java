package com.codingbottle.domain.weather.service;

import com.codingbottle.auth.entity.User;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.exception.ApplicationErrorType;
import com.codingbottle.common.redis.WeatherRedisService;
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
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WebClient webClient;
    private final WeatherRedisService weatherRedisService;

    @Value("${openweathermap.api-key}")
    private String weatherApiKey;

    public WeatherResponse getWeather(User user) {
        try {
            return weatherRedisService.getObjectValues(user.getRegion());
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
            weatherRedisService.setObjectValues(region, getWeather(region));
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.WEATHER_NOT_FOUND, "날씨 정보를 가져오는데 실패했습니다.");
        }
    }

    private WeatherResponse getWeather(Region region) {
        CurrentWeatherRequest currentWeatherRequest = getRequestCurrentWeather(region);

        if (currentWeatherRequest == null) {
            throw new ApplicationErrorException(ApplicationErrorType.WEATHER_NOT_FOUND, "날씨 정보를 가져오는데 실패했습니다.");
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
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new ApplicationErrorException(ApplicationErrorType.WEB_CLIENT_ERROR)))
                .bodyToMono(CurrentWeatherRequest.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                .block();
    }

    @PostConstruct
    private void initAllRegionWeather() {
        getAllRegionWeather();
    }
}

