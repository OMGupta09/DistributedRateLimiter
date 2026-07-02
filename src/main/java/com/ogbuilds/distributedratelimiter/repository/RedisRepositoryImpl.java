package com.ogbuilds.distributedratelimiter.repository;

import com.ogbuilds.distributedratelimiter.model.TokenBucket;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    private final ReactiveRedisTemplate<String, TokenBucket> redisTemplate;

    public RedisRepositoryImpl(ReactiveRedisTemplate<String, TokenBucket> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<TokenBucket> getBucket(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Mono<Void> saveBucket(String key, TokenBucket tokenBucket) {
        return redisTemplate.
                opsForValue().set(key, tokenBucket)
                .then();
    }

    @Override
    public Mono<Void> deleteBucket(String key) {
        return redisTemplate.opsForValue().getAndDelete(key).then();
    }
}
