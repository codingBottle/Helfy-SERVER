package com.codingbottle.redis.domain.quiz.model;

public record RankInfo(
        long rank,
        int score
) {
    public static RankInfo of(long rank, int score) {
        return new RankInfo(rank, score);
    }
}
