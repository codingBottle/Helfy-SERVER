package com.codingbottle.domain.weather.service;

import com.codingbottle.domain.region.entity.Region;
import com.codingbottle.domain.weather.model.CurrentWeatherRequest;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.exception.ApplicationErrorType;
import com.codingbottle.redis.domain.weather.model.WeatherCode;
import com.codingbottle.redis.domain.weather.model.WeatherInfo;
import com.codingbottle.redis.domain.weather.service.WeatherRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherBatchService {
    private final WebClient webClient;
    private final WeatherRedisService weatherRedisService;

    @Value("${openweathermap.api-key}")
    private String weatherApiKey;

    public void setWeather() {
        Arrays.stream(Region.values())
                .forEach(this::setRegionWeather);
    }

    private void setRegionWeather(Region region) {
        try {
            weatherRedisService.setWeatherData(region.name(), getWeather(region));
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.WEATHER_NOT_FOUND, "날씨 정보를 가져오는데 실패했습니다.");
        }
    }

    private WeatherInfo getWeather(Region region) {
        CurrentWeatherRequest currentWeatherRequest = getRequestCurrentWeather(region);

        if (currentWeatherRequest == null) {
            throw new ApplicationErrorException(ApplicationErrorType.WEATHER_NOT_FOUND, "날씨 정보를 가져오는데 실패했습니다.");
        }

        return WeatherInfo.of(WeatherCode.of(currentWeatherRequest.weather().get(0).id()),
                currentWeatherRequest.main().temp(),
                currentWeatherRequest.main().humidity());
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
        setWeather();
    }
}
