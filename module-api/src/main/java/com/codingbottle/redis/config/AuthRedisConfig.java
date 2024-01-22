package com.codingbottle.redis.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRedisRepositories(redisTemplateRef = "authRedisTemplate")
public class AuthRedisConfig extends RedisConfig {
    @Bean
    public RedisConnectionFactory authRedisConnectionFactory() {
        return createLettuceConnectionFactory(4);
    }

    @Bean
    public RedisTemplate<String, Object> authRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(authRedisConnectionFactory());
        return template;
    }

    @Bean(name = "authRedisCacheManager")
    public CacheManager authRedisCacheManager() {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.
                        SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.
                        SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("user", cacheConfiguration.entryTtl(Duration.ofMinutes(30)));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(authRedisConnectionFactory())
                .cacheDefaults(cacheConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
