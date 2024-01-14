package com.codingbottle.domain.user.model;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.region.entity.Region;
import lombok.Builder;

@Builder
public record UserResponse(
        Region region,
        String nickname) {
    public static UserResponse of(User user) {
        return UserResponse.builder()
                .nickname(user.getNickname())
                .region(user.getRegion())
                .build();
    }
}
