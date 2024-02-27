package com.codingbottle.domain.region.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "지역 목록 응답")
public record RegionResponse(
        @Schema(description = "지역 목록", example = "{\"SEOUL\": \"서울\", \"GYEONGGI\": \"경기\"}")
        Map<String, String> region
) {
    public static RegionResponse of(Map<String, String> region) {
        return new RegionResponse(region);
    }
}
