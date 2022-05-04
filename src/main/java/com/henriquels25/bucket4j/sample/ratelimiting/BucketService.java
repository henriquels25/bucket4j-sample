package com.henriquels25.bucket4j.sample.ratelimiting;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Service
public class BucketService {

    private ProxyManager<String> buckets;

    public BucketService(Cache<String, byte[]> cache){
        buckets = new JCacheProxyManager<>(cache);
    }

    public Bucket resolveBucket(String country){
       return buckets.builder().build(country, bucketConfig(country));
    }

    private Supplier<BucketConfiguration> bucketConfig(String country) {
        Bandwidth limit;
        if (country.equals("BR")){
            limit = Bandwidth.simple(10, Duration.ofMinutes(1));
        } else {
            limit = Bandwidth.simple(5, Duration.ofMinutes(1));
        }

        return () -> BucketConfiguration.builder().addLimit(limit).build();
    }

}
