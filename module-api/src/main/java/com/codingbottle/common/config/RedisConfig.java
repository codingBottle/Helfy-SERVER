package com.codingbottle.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactoryForWeather() {
        RedisStandaloneConfiguration config1 = new RedisStandaloneConfiguration();
        config1.setHostName(redisProperties.getHost());
        config1.setPort(redisProperties.getPort());
        config1.setDatabase(0);

        return new LettuceConnectionFactory(config1);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactoryForLikes() {
        RedisStandaloneConfiguration config2 = new RedisStandaloneConfiguration();
        config2.setHostName(redisProperties.getHost());
        config2.setPort(redisProperties.getPort());
        config2.setDatabase(1);

        return new LettuceConnectionFactory(config2);
    }

    @Bean
    public RedisTemplate<String, Object> weatherRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactoryForWeather());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(RedisSerializer.string());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> likesRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactoryForLikes());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(RedisSerializer.string());
        return redisTemplate;
    }
}
