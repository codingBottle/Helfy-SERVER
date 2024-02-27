package com.codingbottle.domain.user.model;

import com.codingbottle.redis.domain.quiz.model.RankInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 정보와 랭킹 정보 응답")
public record UserResponseWithRankInfo(
        @Schema(description = "유저 정보", implementation = UserResponse.class)
        UserResponse userInfo,
        @Schema(description = "랭킹 정보", implementation = RankInfo.class)
        RankInfo rankInfo
) {
    public static UserResponseWithRankInfo of(UserResponse user, RankInfo rank) {
        return new UserResponseWithRankInfo(user, rank);
    }
}
