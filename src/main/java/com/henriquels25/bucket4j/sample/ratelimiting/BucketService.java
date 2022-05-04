package com.henriquels25.bucket4j.sample.ratelimiting;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BucketService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String country){
        return cache.computeIfAbsent(country, this::newBucket);
    }

    private Bucket newBucket(String country) {
        Bandwidth limit;
        if (country.equals("BR")){
            limit = Bandwidth.simple(10, Duration.ofMinutes(1));
        } else {
            limit = Bandwidth.simple(5, Duration.ofMinutes(1));
        }

        return Bucket.builder().addLimit(limit).build();
    }

}
