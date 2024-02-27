package com.codingbottle.domain.category.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "카테고리 목록 응답")
public record CategoryResponse(
        @Schema(description = "카테고리 목록", example = "{\"FLOOD\": \"홍수\", \"TYPHOON\": \"태풍\"}")
        Map<String, String> categories
) {
    public static CategoryResponse from(Map<String, String> categories) {
        return new CategoryResponse(categories);
    }
}
