package com.codingbottle.redis.domain.quiz.service;

import com.codingbottle.redis.domain.quiz.model.UserInfo;
import com.codingbottle.redis.domain.quiz.model.RankInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class QuizRankRedisService {
    private final RedisTemplate<Object,Object> quizRankRedisTemplate;
    private static final String QUIZ_RANK_KEY = "QUIZ_RANK";

    public int getScore(UserInfo user) {
        Double score = quizRankRedisTemplate.opsForZSet().score(QUIZ_RANK_KEY, user);
        return getScoreAnInt(Optional.ofNullable(score));
    }

    public void addScore(UserInfo user, int score) {
        quizRankRedisTemplate.opsForZSet().add(QUIZ_RANK_KEY, user, score);
    }

    public void updateScore(UserInfo user, int score) {
        int newScore = getScore(user) + score;
        addScore(user, newScore);
    }

    public List<UserInfo> getUsersRank() {
        Set<ZSetOperations.TypedTuple<Object>> rankSet = quizRankRedisTemplate.opsForZSet().reverseRangeWithScores(QUIZ_RANK_KEY, 0, 3);
        Objects.requireNonNull(rankSet);

        return rankSet.stream()
                .map(this::convertToUserScore)
                .toList();
    }

    public int removeUserWithScore(UserInfo user) {
        int score = getScore(user);
        remove(user);
        return score;
    }

    public RankInfo getRankInfo(UserInfo user) {
        Long rank = getRank(user);
        int score = getScore(user);

        if (rank == null || score == 0) {
            return RankInfo.of(0, score);
        }
        return RankInfo.of(rank + 1, score);
    }

    private void remove(UserInfo user) {
        quizRankRedisTemplate.opsForZSet().remove(QUIZ_RANK_KEY, user);
    }

    private static int getScoreAnInt(Optional<Double> score) {
        return score.orElse(0.0).intValue();
    }

    private UserInfo convertToUserScore(ZSetOperations.TypedTuple<Object> rank) {
        return (UserInfo) rank.getValue();
    }

    private Long getRank(UserInfo user) {
        return quizRankRedisTemplate.opsForZSet().reverseRank(QUIZ_RANK_KEY, user);
    }
}

