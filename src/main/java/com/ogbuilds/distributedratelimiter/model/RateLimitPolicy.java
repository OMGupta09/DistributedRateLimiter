package com.ogbuilds.distributedratelimiter.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class RateLimitPolicy {

    long capacity;
    long refillTokens;
    Duration refillDuration;

    public RateLimitPolicy(long capacity, long refillTokens, Duration refillDuration) {
        this.capacity = capacity;
        this.refillTokens = refillTokens;
        this.refillDuration = refillDuration;
    }
}
