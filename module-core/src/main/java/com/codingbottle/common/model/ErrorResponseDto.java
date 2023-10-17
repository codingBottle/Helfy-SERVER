package com.codingbottle.common.model;

public record ErrorResponseDto(
        String errorCode,
        String reason
) {
}