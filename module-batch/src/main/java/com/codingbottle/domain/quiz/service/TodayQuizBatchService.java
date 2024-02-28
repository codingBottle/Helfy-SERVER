package com.codingbottle.domain.quiz.service;

import com.codingbottle.redis.domain.quiz.service.TodayQuizRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodayQuizBatchService {
    private final TodayQuizRedisService todayQuizRedisService;

    public void initializeData() {
        todayQuizRedisService.initializeData();
        log.info("오늘의 퀴즈 사용자 초기화 완료");
    }
}
