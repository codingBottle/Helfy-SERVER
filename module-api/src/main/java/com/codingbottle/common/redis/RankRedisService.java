package com.codingbottle.common.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RankRedisService {
    private final RedisTemplate<String, String> rankRedisTemplate;
    private ZSetOperations<String, String> zSetOperations;

    @PostConstruct
    public void init() {
        zSetOperations = rankRedisTemplate.opsForZSet();
    }
    public Double ZSetGetScore(String key, String value) {
        return zSetOperations.score(key, value);
    }

    public void ZSetAdd(String key, String value, Double score) {
        zSetOperations.add(key, value, score);
    }

    public void ZSetDelete(String key, String value) {
        zSetOperations.remove(key, value);
    }

    public void ZSetAddOrUpdate(String key, String value, Double score) {
        Double presentScore = ZSetGetScore(key, value);

        if (presentScore == null) {
            ZSetAdd(key, value, score);
        } else {
            Double totalScore = presentScore + score;
            if (isZeroOrLess(totalScore)) {
                ZSetDelete(key, value);
            } else {
                ZSetAdd(key, value, presentScore + score);
            }
        }
    }

    public Set<String> getZset(String key, long end) {
        return zSetOperations.reverseRange(key, 0, end);
    }

    private boolean isZeroOrLess(Double value) {
        return value <= 0.0;
    }
}
