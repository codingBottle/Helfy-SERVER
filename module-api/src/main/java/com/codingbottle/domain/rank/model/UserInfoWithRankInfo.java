package com.codingbottle.domain.rank.model;

import com.codingbottle.redis.domain.quiz.model.UserInfo;
import com.codingbottle.redis.domain.quiz.model.RankInfo;

public record UserInfoWithRankInfo(
        UserInfo userInfo,
        RankInfo rankInfo
) {
    public static UserInfoWithRankInfo of(UserInfo user, RankInfo rank) {
        return new UserInfoWithRankInfo(user, rank);
    }
}
