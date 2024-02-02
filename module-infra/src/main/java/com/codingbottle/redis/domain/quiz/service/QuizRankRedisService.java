package com.codingbottle.redis.domain.quiz.service;

import com.codingbottle.redis.domain.quiz.model.QuizRankUserData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class QuizRankRedisService {
    private final RedisTemplate<Object,Object> quizRankRedisTemplate;
    private static final String QUIZ_RANK_KEY = "QUIZ_RANK";

    public int getScore(QuizRankUserData user) {
        Double score = quizRankRedisTemplate.opsForZSet().score(QUIZ_RANK_KEY, user);
        return getScoreAnInt(Optional.ofNullable(score));
    }

    public void addScore(QuizRankUserData user, int score) {
        quizRankRedisTemplate.opsForZSet().add(QUIZ_RANK_KEY, user, score);
    }

    public void updateScore(QuizRankUserData user, int score) {
        int newScore = getScore(user) + score;
        addScore(user, newScore);
    }

    public List<QuizRankUserData> getUsersRank() {
        Set<ZSetOperations.TypedTuple<Object>> rankSet = quizRankRedisTemplate.opsForZSet().reverseRangeWithScores(QUIZ_RANK_KEY, 0, 3);
        Objects.requireNonNull(rankSet);

        return rankSet.stream()
                .map(this::convertToUserScore)
                .collect(Collectors.toList());
    }

    private void remove(QuizRankUserData user) {
        quizRankRedisTemplate.opsForZSet().remove(QUIZ_RANK_KEY, user);
    }

    public int removeUserWithScore(QuizRankUserData user) {
        int score = getScore(user);
        remove(user);
        return score;
    }

    private static int getScoreAnInt(Optional<Double> score) {
        return score.orElse(0.0).intValue();
    }

    private QuizRankUserData convertToUserScore(ZSetOperations.TypedTuple<Object> rank) {
        return(QuizRankUserData) rank.getValue();
    }


    public long getRank(QuizRankUserData user) {
        long rank = Optional.ofNullable(quizRankRedisTemplate.opsForZSet().rank(QUIZ_RANK_KEY, user)).orElse(-1L);
        return rank + 1;
    }
}

