package com.codingbottle.common.redis.service;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.common.exception.ApplicationErrorType;
import com.codingbottle.common.redis.model.CacheUser;
import com.codingbottle.domain.rank.model.UserRankResponse;
import com.codingbottle.domain.user.model.UpdateUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class QuizRankRedisService {
    private final ZSetOperations<String, String> zSetOperations;
    private final ObjectMapper objectMapper;

    private static final String QUIZ_RANK_KEY = "QUIZ_RANK";

    @EventListener
    public void handleUserUpdate(UpdateUser updateUser) {
        int score = removeUserWithScore(updateUser.before());
        addScore(updateUser.after(), score);
    }

    public QuizRankRedisService(@Qualifier("quizRankRedisTemplate") RedisTemplate<String, String> quizRankRedisTemplate) {
        this.zSetOperations = quizRankRedisTemplate.opsForZSet();
        this.objectMapper = new ObjectMapper();
    }

    public int getScore(CacheUser user) {
        Double score = zSetOperations.score(QUIZ_RANK_KEY, convertToJson(user));
        return getScoreAnInt(Optional.ofNullable(score));
    }

    public void addScore(CacheUser user, int score) {
        zSetOperations.add(QUIZ_RANK_KEY, convertToJson(user), score);
    }

    public void updateScore(User user, int score) {
        CacheUser cacheUser = CacheUser.from(user);
        int newScore = getScore(cacheUser) + score;
        addScore(cacheUser, newScore);
    }

    public List<UserRankResponse> getUsersRank() {
        Set<ZSetOperations.TypedTuple<String>> rankSet = zSetOperations.reverseRangeWithScores(QUIZ_RANK_KEY, 0, 3);
        Objects.requireNonNull(rankSet);

        return rankSet.stream()
                .map(this::convertToUserScore)
                .collect(Collectors.toList());
    }

    private void remove(CacheUser user) {
        zSetOperations.remove(QUIZ_RANK_KEY, convertToJson(user));
    }

    private int removeUserWithScore(CacheUser user) {
        int score = getScore(user);
        remove(user);
        return score;
    }

    private static int getScoreAnInt(Optional<Double> score) {
        return score.orElse(0.0).intValue();
    }

    private UserRankResponse convertToUserScore(ZSetOperations.TypedTuple<String> rank) {
        CacheUser user = convertToUser(rank.getValue());
        return UserRankResponse.of(getRank(user) + 1, user.nickname(), Objects.requireNonNull(rank.getScore()).intValue());
    }

    private CacheUser convertToUser(String userJson) {
        try {
            return objectMapper.readValue(userJson, CacheUser.class);
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.JSON_PROCESSING_EXCEPTION);
        }
    }

    private String convertToJson(CacheUser user) {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.JSON_PROCESSING_EXCEPTION);
        }
    }

    public UserRankResponse getUserRank(User user) {
        CacheUser cacheUser = CacheUser.from(user);
        int score = getScore(cacheUser);
        return UserRankResponse.of(getRank(cacheUser) + 1, cacheUser.nickname(), score);
    }

    private Long getRank(CacheUser cacheUser) {
        Long rank = zSetOperations.rank(QUIZ_RANK_KEY, convertToJson(cacheUser));
        return Optional.ofNullable(rank).orElse(-1L);
    }
}

