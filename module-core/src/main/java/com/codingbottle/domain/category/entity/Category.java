package com.codingbottle.domain.category.entity;

import lombok.Getter;

@Getter
public enum Category {
    FLOODING("침수"),
    TYPHOON("태풍"),
    HEAVY_RAIN("호우"),
    LIGHTNING("낙뢰"),
    STRONG_WIND("강풍"),
    WIND_AND_WAVES("풍랑"),
    HEAVY_SNOW("폭설"),
    COLD_WAVE("한파"),
    HEAT_WAVE("폭염"),
    YELLOW_DUST("황사"),
    EARTHQUAKE("지진"),
    TIDAL_WAVE("해일"),
    TSUNAMI("지진해일"),
    VOLCANIC_ERUPTION("화산폭발"),
    DROUGHT("가뭄"),
    FLOOD("홍수"),
    LANDSLIDE("산사태"),
    RISING_SEA_LEVEL("해수면 상승"),
    THE_FALL_OF_NATURAL_SPACE_OBJECTS("자연우주물체추락"),
    SPACE_PROPAGATION_DISASTER("우주전파재난"),
    GREEN_TIDE("녹조"),
    RED_TIDE("적조");

    private final String detail;

    Category(String detail) {
        this.detail = detail;
    }
}
