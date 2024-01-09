package com.codingbottle.domain.rank.model;

public record UserRankResponse(
        Long rank,
        String nickname,
        int score
) {
    public static UserRankResponse of(Long rank, String nickname, int score) {
        return new UserRankResponse(rank, nickname, score);
    }
}
