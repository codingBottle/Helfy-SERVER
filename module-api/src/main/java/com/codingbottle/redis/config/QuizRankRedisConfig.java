package com.codingbottle.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories(redisTemplateRef = "quizRankRedisTemplate")
public class QuizRankRedisConfig extends RedisConfig {
    @Bean
    public RedisConnectionFactory QuizRankRedisConnectionFactory() {
        return createLettuceConnectionFactory(3);
    }

    @Bean
    public RedisTemplate<String, String> quizRankRedisTemplate() {
        RedisTemplate<String, String>  template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setConnectionFactory(QuizRankRedisConnectionFactory());
        template.afterPropertiesSet();
        return template;
    }
}
