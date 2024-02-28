package com.codingbottle.redis.domain.quiz.config;

import com.codingbottle.redis.config.RedisConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
@EnableRedisRepositories(redisTemplateRef = "todayQuizRedisTemplate")
public class TodayQuizRedisConfig extends RedisConfig {
    @Bean
    public RedisConnectionFactory todayQuizRedisConnectionFactory() {
        return createLettuceConnectionFactory(5);
    }

    @Bean
    public RedisTemplate<Long, Boolean> todayQuizRedisTemplate() {
        RedisTemplate<Long, Boolean> template = new RedisTemplate<>();
        template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(todayQuizRedisConnectionFactory());
        template.afterPropertiesSet();
        return template;
    }
}
