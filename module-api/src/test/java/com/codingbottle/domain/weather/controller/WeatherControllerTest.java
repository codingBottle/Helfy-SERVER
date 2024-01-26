package com.codingbottle.domain.weather.controller;

import com.codingbottle.docs.util.RestDocsTest;
import com.codingbottle.redis.domain.weather.model.WeatherCode;
import com.codingbottle.redis.domain.weather.model.WeatherResponse;
import com.codingbottle.domain.weather.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.codingbottle.docs.util.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("WeatherController 테스트")
@ContextConfiguration(classes = WeatherController.class)
@WebMvcTest(value = WeatherController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class WeatherControllerTest extends RestDocsTest {
    @MockBean
    WeatherService weatherService;

    @Test
    @DisplayName("사용자 지역의 날씨 조회")
    void get_current_weather() throws Exception {
        //given
        WeatherResponse weatherResponse = new WeatherResponse(WeatherCode.CLEAR, 20.0, 90);

        given(weatherService.getWeather(any())).willReturn(weatherResponse);
        //when & then
        mvc.perform(get("/api/v1/weather")
                        .header("Authorization", "Bearer FirebaseToken"))
                .andExpect(status().isOk())
                .andDo(document("current-weather",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthorizationHeader()
                        , responseFields(
                                fieldWithPath("weatherCode").description("날씨 코드 예시: " +
                                        Arrays.stream(WeatherCode.values())
                                                .map(Enum::name)
                                                .collect(Collectors.joining(", "))),
                                fieldWithPath("temp").description("온도"),
                                fieldWithPath("humidity").description("습도")
                        )));
    }
}