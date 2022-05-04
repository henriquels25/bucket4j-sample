package com.henriquels25.bucket4j.sample.controller;

import com.henriquels25.bucket4j.sample.ratelimiting.BucketService;
import com.henriquels25.bucket4j.sample.service.UserService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BucketService bucketService;

    @GetMapping
    public ResponseEntity<List<User>> getUsersByCoutry(@RequestParam String country){
        Bucket bucket = bucketService.resolveBucket(country);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()){
            return ResponseEntity.ok()
                    .header("X-Rate-Limit-Remaining",
                            Long.toString(probe.getRemainingTokens()))
                    .body(userService.getUsersByCountry(country));
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .header("X-Rate-Limit-Retry-After-Seconds",
                            String.valueOf(waitForRefill))
                    .build();
        }
    }


}
