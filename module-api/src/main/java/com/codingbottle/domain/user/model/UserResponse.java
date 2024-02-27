package com.codingbottle.domain.user.model;

import com.codingbottle.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserResponse(
        @Schema(description = "지역", example = "서울")
        String region,
        @Schema(description = "닉네임", example = "홍길동")
        String nickname,
        @Schema(description = "프로필 이미지 URL", example = "https://hellobf.co.kr/wp-content/uploads/2016/02/smile-puppy-01-1280x720.jpg")
        String profileImageUrl
) {
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .nickname(user.getNickname())
                .region(user.getRegion().getDetail())
                .profileImageUrl(user.getProfileImage())
                .build();
    }
}
