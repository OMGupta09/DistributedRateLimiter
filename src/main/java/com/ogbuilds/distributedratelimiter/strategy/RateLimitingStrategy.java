package com.ogbuilds.distributedratelimiter.strategy;

import com.ogbuilds.distributedratelimiter.model.RateLimitPolicy;
import com.ogbuilds.distributedratelimiter.model.TokenBucket;

public interface RateLimitingStrategy {
    boolean tryConsume(TokenBucket tokenBucket, RateLimitPolicy policy);
}
