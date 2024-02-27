package com.codingbottle.domain.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "프로필 이미지 업데이트 요청")
public record UserProfileImageUpdateRequest(
        @NotNull
        @Schema(description = "이미지 ID", example = "1")
        Long id
) {
}
