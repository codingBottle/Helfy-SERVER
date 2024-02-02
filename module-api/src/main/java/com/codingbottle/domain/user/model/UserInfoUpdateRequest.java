package com.codingbottle.domain.user.model;


import com.codingbottle.domain.region.entity.Region;

public record UserInfoUpdateRequest(
        String nickname,
        Region region
) {
}
