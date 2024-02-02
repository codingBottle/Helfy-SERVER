package com.codingbottle.domain.rank.model;

import java.util.List;

public record UsersRankResponse(
        List<UserInfoWithRankInfo> userInfoWithRankInfoList
) {
    public static UsersRankResponse of(List<UserInfoWithRankInfo> userInfoWithRankInfoList) {
        return new UsersRankResponse(userInfoWithRankInfoList);
    }
}
