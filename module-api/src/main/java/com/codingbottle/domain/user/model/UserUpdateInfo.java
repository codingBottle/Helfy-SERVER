package com.codingbottle.domain.user.model;

import com.codingbottle.domain.region.entity.Region;

public record UserUpdateInfo(
        String nickname,
        Region region
) {
    public static UserUpdateInfo of(String nickname, Region region) {
        return new UserUpdateInfo(nickname, region);
    }
}
