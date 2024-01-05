package com.codingbottle.common.redis.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class QuizRankRedisService {
    private final RedisTemplate<String, String> quizRankRedisTemplate;
    private ZSetOperations<String, String> zSetOperations;

    private static final String QUIZ_RANK_KEY = "quiz_rank";

    public QuizRankRedisService(@Qualifier("quizRankRedisTemplate") RedisTemplate<String, String> quizRankRedisTemplate) {
        this.quizRankRedisTemplate = quizRankRedisTemplate;
    }

    @PostConstruct
    public void init() {
        zSetOperations = quizRankRedisTemplate.opsForZSet();
    }

    public Optional<Double> getScore(String user) {
        return Optional.ofNullable(zSetOperations.score(QUIZ_RANK_KEY, user));
    }

    public void addScore(String user, int score) {
        zSetOperations.add(QUIZ_RANK_KEY, user, score);
    }

    public void updateScore(String user, int score) {
        int newScore = (int) (getScore(user).orElse(0.0) + score);
        addScore(user, newScore);
    }
}
