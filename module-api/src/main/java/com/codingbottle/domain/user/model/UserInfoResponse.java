package com.codingbottle.domain.user.model;

import com.codingbottle.domain.region.entity.Region;
import com.codingbottle.domain.user.entity.User;

public record UserInfoResponse(
        String nickname,
        Region region,
        Long rank,
        int score
) {
    public static UserInfoResponse of(User user, Long rank, int score) {
        return new UserInfoResponse(user.getNickname(), user.getRegion(), rank, score);
    }
}
