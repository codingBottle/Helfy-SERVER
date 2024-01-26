package com.codingbottle.redis.domain.quiz.config;

import com.codingbottle.redis.config.RedisConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
@EnableRedisRepositories(redisTemplateRef = "quizRankRedisTemplate")
public class QuizRankRedisConfig extends RedisConfig {
    @Bean
    public RedisConnectionFactory QuizRankRedisConnectionFactory() {
        return createLettuceConnectionFactory(3);
    }

    @Bean
    public RedisTemplate<Object, Object> quizRankRedisTemplate() {
        RedisTemplate<Object, Object>  template = new RedisTemplate<>();
        template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(QuizRankRedisConnectionFactory());
        template.afterPropertiesSet();
        return template;
    }
}
