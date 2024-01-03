package com.codingbottle.common.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(redisTemplateRef = "likesRedisTemplate")
public class LikeRedisConfig extends RedisConfig {
    @Bean
    public RedisConnectionFactory deviceControlRedisConnectionFactory() {
        return createLettuceConnectionFactory(2); // Redis DB 선택
    }

    @Bean
    public RedisTemplate<Long, Long> likesRedisTemplate() {
        RedisTemplate<Long, Long>  template = new RedisTemplate<>();
        template.setConnectionFactory(deviceControlRedisConnectionFactory());
        template.afterPropertiesSet();
        return template;
    }
}
