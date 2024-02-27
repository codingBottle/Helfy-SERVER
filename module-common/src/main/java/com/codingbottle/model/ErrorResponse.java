package com.codingbottle.model;

public record ErrorResponse(
        String errorCode,
        String reason
) {
    public static ErrorResponse of(String errorCode, String reason) {
        return new ErrorResponse(errorCode, reason);
    }
}