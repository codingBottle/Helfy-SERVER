package com.codingbottle.redis.service;

import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.rank.model.UserRankResponse;
import com.codingbottle.domain.user.event.UpdateUserInfoRedisEvent;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.exception.ApplicationErrorType;
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
    public void handleUserUpdate(UpdateUserInfoRedisEvent event) {
        int score = removeUserWithScore(event.getUser());
        User user = event.getUser();
        addScore(user.updateInfo(event.getUserUpdateInfo().nickname(), event.getUserUpdateInfo().region()), score);
    }

    public QuizRankRedisService(@Qualifier("quizRankRedisTemplate") RedisTemplate<String, String> quizRankRedisTemplate) {
        this.zSetOperations = quizRankRedisTemplate.opsForZSet();
        this.objectMapper = new ObjectMapper();
    }

    public int getScore(User user) {
        Double score = zSetOperations.score(QUIZ_RANK_KEY, convertToJson(user));
        return getScoreAnInt(Optional.ofNullable(score));
    }

    public void addScore(User user, int score) {
        zSetOperations.add(QUIZ_RANK_KEY, convertToJson(user), score);
    }

    public void updateScore(User user, int score) {
        int newScore = getScore(user) + score;
        addScore(user, newScore);
    }

    public List<UserRankResponse> getUsersRank() {
        Set<ZSetOperations.TypedTuple<String>> rankSet = zSetOperations.reverseRangeWithScores(QUIZ_RANK_KEY, 0, 3);
        Objects.requireNonNull(rankSet);

        return rankSet.stream()
                .map(this::convertToUserScore)
                .collect(Collectors.toList());
    }

    private void remove(User user) {
        zSetOperations.remove(QUIZ_RANK_KEY, convertToJson(user));
    }

    private int removeUserWithScore(User user) {
        int score = getScore(user);
        remove(user);
        return score;
    }

    private static int getScoreAnInt(Optional<Double> score) {
        return score.orElse(0.0).intValue();
    }

    private UserRankResponse convertToUserScore(ZSetOperations.TypedTuple<String> rank) {
        User user = convertToUser(rank.getValue());
        return UserRankResponse.of(getRank(user) + 1, user.getNickname(), Objects.requireNonNull(rank.getScore()).intValue());
    }

    private User convertToUser(String userJson) {
        try {
            return objectMapper.readValue(userJson, User.class);
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.JSON_PROCESSING_EXCEPTION);
        }
    }

    private String convertToJson(User user) {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new ApplicationErrorException(ApplicationErrorType.JSON_PROCESSING_EXCEPTION);
        }
    }

    public UserRankResponse getUserRank(User user) {
        int score = getScore(user);
        return UserRankResponse.of(getRank(user) + 1, user.getNickname(), score);
    }

    private Long getRank(User user) {
        Long rank = zSetOperations.rank(QUIZ_RANK_KEY, convertToJson(user));
        return Optional.ofNullable(rank).orElse(-1L);
    }
}

