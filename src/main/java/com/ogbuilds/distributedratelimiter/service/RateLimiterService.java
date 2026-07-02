package com.ogbuilds.distributedratelimiter.service;

import com.ogbuilds.distributedratelimiter.config.RateLimiterProperties;
import com.ogbuilds.distributedratelimiter.model.RateLimitPolicy;
import com.ogbuilds.distributedratelimiter.model.TokenBucket;
import com.ogbuilds.distributedratelimiter.repository.RedisRepository;
import com.ogbuilds.distributedratelimiter.strategy.RateLimitingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RedisRepository redisRepository;
    private final RateLimitingStrategy rateLimitingStrategy;
    private final RateLimiterProperties rateLimiterProperties;

    public Mono<Boolean> tryConsume(String clientId) {

        System.out.println("Inside RateLimiterService");

        String key = rateLimiterProperties.getRedisPrefix() + clientId;

        RateLimitPolicy policy = getPolicy();

        return redisRepository
                .getBucket(key)
                .switchIfEmpty(createBucket(key, policy))
                .flatMap(bucket -> {

                    boolean allowed =
                            rateLimitingStrategy.tryConsume(bucket, policy);

                    return redisRepository
                            .saveBucket(key, bucket)
                            .thenReturn(allowed);

                });
    }

    private RateLimitPolicy getPolicy() {
        return new RateLimitPolicy(
                rateLimiterProperties.getCapacity(),
                rateLimiterProperties.getRefillTokens(),
                rateLimiterProperties.getRefillDuration()
        );
    }

    private Mono<TokenBucket> createBucket(String key, RateLimitPolicy policy) {
        return Mono.defer(() -> {
                    TokenBucket bucket = TokenBucket.builder()
                            .availableTokens(policy.getCapacity())
                            .lastRefillTime(Instant.now())
                            .build();

                    return redisRepository.saveBucket(key, bucket)
                            .thenReturn(bucket);
                }
        );
    }
}