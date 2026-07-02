package com.ogbuilds.distributedratelimiter.repository;

import com.ogbuilds.distributedratelimiter.model.TokenBucket;
import reactor.core.publisher.Mono;

public interface RedisRepository {

    Mono<TokenBucket> getBucket(String key);

    Mono<Void> saveBucket(String key, TokenBucket tokenBucket);

    Mono<Void> deleteBucket(String key);

}
