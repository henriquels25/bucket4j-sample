package com.henriquels25.bucket4j.sample.ratelimiting;

import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.context.annotation.Bean;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;

@org.springframework.context.annotation.Configuration
public class CacheConfig {

    @Bean
    public Cache<String, byte[]> buildCache(RedissonClient redissonClient) {
        Configuration<String, byte[]> jcacheConfig = new MutableConfiguration<>();

        Config reddisonCfg = redissonClient.getConfig();

        Configuration<String, byte[]> config =
                RedissonConfiguration.fromConfig(reddisonCfg, jcacheConfig);

        CacheManager manager = Caching.getCachingProvider().getCacheManager();
        return manager.createCache("users-country-cache", config);
    }

}

