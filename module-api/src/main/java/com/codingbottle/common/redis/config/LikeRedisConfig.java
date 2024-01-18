package com.codingbottle.common.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
@EnableRedisRepositories(redisTemplateRef = "likesRedisTemplate")
public class LikeRedisConfig extends RedisConfig {
    @Bean
    public RedisConnectionFactory deviceControlRedisConnectionFactory() {
        return createLettuceConnectionFactory(2); // Redis DB 선택
    }

    @Bean
    public RedisTemplate<Object, Object> likesRedisTemplate() {
        RedisTemplate<Object, Object>  template = new RedisTemplate<>();
        template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(deviceControlRedisConnectionFactory());
        template.afterPropertiesSet();
        return template;
    }
}
