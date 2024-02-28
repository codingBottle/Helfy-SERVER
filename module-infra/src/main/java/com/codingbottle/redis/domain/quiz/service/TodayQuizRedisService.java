package com.codingbottle.redis.domain.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TodayQuizRedisService {
    private final RedisTemplate<Long, Boolean> todayQuizRedisTemplate;

    public boolean isAlreadySolved(Long userId) {
        return Boolean.TRUE.equals(todayQuizRedisTemplate.opsForSet().isMember(userId, true));
    }

    public void setSolved(Long userId) {
        todayQuizRedisTemplate.opsForSet().add(userId, true);
    }

    public void initializeData() {
        try {
            Objects.requireNonNull(todayQuizRedisTemplate.getConnectionFactory())
                    .getConnection()
                    .serverCommands()
                    .flushDb();
        } catch (Exception e) {
            throw new RuntimeException("오늘의 퀴즈 레디스 초기화에 실패했습니다.");
        }
    }
}
