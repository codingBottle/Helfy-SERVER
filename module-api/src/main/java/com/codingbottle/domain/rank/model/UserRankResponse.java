package com.codingbottle.domain.rank.model;

import com.codingbottle.redis.domain.quiz.model.QuizRankUserData;

public record UserRankResponse(
        Long rank,
        QuizRankUserData user,
        int score
) {
    public static UserRankResponse of(Long rank, QuizRankUserData user, int score) {
        return new UserRankResponse(rank, user, score);
    }
}
