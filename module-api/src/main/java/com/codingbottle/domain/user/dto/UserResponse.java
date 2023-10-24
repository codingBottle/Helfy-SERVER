package com.codingbottle.domain.user.dto;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.region.entity.Region;
import lombok.Builder;

@Builder
public record UserResponse(
        String username,
        Region region
) {
    public static UserResponse of(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .region(user.getRegion())
                .build();
    }
}
