package com.codingbottle.domain.user.model;

import com.codingbottle.redis.domain.quiz.model.RankInfo;

public record UserResponseWithRankInfo(
        UserResponse userInfo,
        RankInfo rankInfo
) {
    public static UserResponseWithRankInfo of(UserResponse user, RankInfo rank) {
        return new UserResponseWithRankInfo(user, rank);
    }
}
