package com.codingbottle.domain.rank.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "유저 랭킹 목록 응답")
public record UsersRankResponse(
        @Schema(description = "유저 랭킹 목록")
        List<UserInfoWithRankInfo> userInfoWithRankInfoList
) {
    public static UsersRankResponse of(List<UserInfoWithRankInfo> userInfoWithRankInfoList) {
        return new UsersRankResponse(userInfoWithRankInfoList);
    }
}
