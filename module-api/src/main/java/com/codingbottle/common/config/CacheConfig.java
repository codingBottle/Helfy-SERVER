package com.codingbottle.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@EnableCaching
@Configuration
public class CacheConfig {
    @Primary
    @Bean(name = "postCacheManager")
    public CacheManager postCacheManager() {
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager();
        concurrentMapCacheManager.setAllowNullValues(false);
        concurrentMapCacheManager.setCacheNames(List.of("posts"));
        return concurrentMapCacheManager;
    }
}
