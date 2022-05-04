package com.henriquels25.bucket4j.sample;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class BucketSampleUsage {

    @Test
    void creatingABucketSample(){
        Bandwidth limit = Bandwidth.simple(100, Duration.ofMinutes(1));
        Bucket bucket = Bucket.builder().addLimit(limit).build();

        for (int x=0; x < 100; x++) {
            boolean consumed = bucket.tryConsume(1);
            assertThat(consumed).isTrue();
        }

        boolean consumed = bucket.tryConsume(1);
        assertThat(consumed).isFalse();

        bucket.addTokens(1);
        consumed = bucket.tryConsume(1);
        assertThat(consumed).isTrue();
    }

}
