package com.codingbottle.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
@EnableRedisRepositories
public class LikeRedisConfig extends RedisConfig {
    @Bean
    public RedisConnectionFactory deviceControlRedisConnectionFactory() {
        return createLettuceConnectionFactory(2); // Redis DB 선택
    }

    @Bean
    @Qualifier("likesRedisTemplate")
    public RedisTemplate<Long, Long> likesRedisTemplate() {
        RedisTemplate<Long, Long>  template = new RedisTemplate<>();
        template.setConnectionFactory(deviceControlRedisConnectionFactory());
        template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
