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
        RedisStandaloneConfiguration weatherRedisConfig = new RedisStandaloneConfiguration();
        weatherRedisConfig.setHostName(redisProperties.getHost());
        weatherRedisConfig.setPort(redisProperties.getPort());
        weatherRedisConfig.setDatabase(0);

        return new LettuceConnectionFactory(weatherRedisConfig);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactoryForLikes() {
        RedisStandaloneConfiguration likesRedisConfig = new RedisStandaloneConfiguration();
        likesRedisConfig.setHostName(redisProperties.getHost());
        likesRedisConfig.setPort(redisProperties.getPort());
        likesRedisConfig.setDatabase(1);

        return new LettuceConnectionFactory(likesRedisConfig);
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
    public RedisTemplate<Long, Long> likesRedisTemplate() {
        RedisTemplate<Long, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactoryForLikes());
        redisTemplate.setDefaultSerializer(RedisSerializer.java());
        return redisTemplate;
    }
}
