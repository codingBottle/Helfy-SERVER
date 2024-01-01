package com.codingbottle.common.redis.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RankRedisConfig extends RedisConfig {
    @Bean
    public RedisConnectionFactory deviceControlRedisConnectionFactory2() {
        return createLettuceConnectionFactory(2);
    }

    @Bean
    @Qualifier("rankRedisTemplate")
    public RedisTemplate<String, Object> rankRedisTemplate() {
        RedisTemplate<String, Object>  template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setConnectionFactory(deviceControlRedisConnectionFactory2());
        template.afterPropertiesSet();
        return template;
    }
}
