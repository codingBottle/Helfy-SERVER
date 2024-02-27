package com.codingbottle.domain.rank.model;

import com.codingbottle.redis.domain.quiz.model.UserInfo;
import com.codingbottle.redis.domain.quiz.model.RankInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 정보와 랭크 정보")
public record UserInfoWithRankInfo(
        @Schema(description = "유저 정보", implementation = UserInfo.class)
        UserInfo userInfo,
        @Schema(description = "랭크 정보", implementation = RankInfo.class)
        RankInfo rankInfo
) {
    public static UserInfoWithRankInfo of(UserInfo user, RankInfo rank) {
        return new UserInfoWithRankInfo(user, rank);
    }
}
