package com.codingbottle.domain.user.model;


import com.codingbottle.domain.region.entity.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "유저 정보 수정 요청시 사용되는 데이터 모델")
public record UserInfoUpdateRequest(
        @NotBlank(message = "닉네임은 필수입니다.")
        @Schema(description = "닉네임", example = "김코딩")
        String nickname,
        @NotNull(message = "지역은 필수입니다.")
        @Schema(description = "지역", example = "SEOUL")
        Region region
) {
}
