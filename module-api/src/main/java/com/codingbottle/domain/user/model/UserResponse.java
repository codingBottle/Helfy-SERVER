package com.codingbottle.domain.user.model;

import com.codingbottle.auth.entity.User;
import com.codingbottle.domain.region.entity.Region;
import lombok.Builder;

@Builder
public record UserResponse(
        String username,
        String picture,
        Region region
) {
    public static UserResponse of(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .picture(user.getPicture())
                .region(user.getRegion())
                .build();
    }
}
