package com.ogbuilds.distributedratelimiter.strategy;

import com.ogbuilds.distributedratelimiter.model.RateLimitPolicy;
import com.ogbuilds.distributedratelimiter.model.TokenBucket;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class TokenBucketStrategy implements RateLimitingStrategy{


    @Override
    public boolean tryConsume(TokenBucket tokenBucket, RateLimitPolicy policy) {
        refill(tokenBucket,policy);

        if(tokenBucket.getAvailableTokens() == 0)
        {
            return false;
        }

        tokenBucket.setAvailableTokens(tokenBucket.getAvailableTokens()-1);

        return true;
    }

    private void refill(TokenBucket tokenBucket, RateLimitPolicy policy)
    {
        Instant now = Instant.now();

        Duration elapsed = Duration.between(tokenBucket.getLastRefillTime(),now);

        long elapsedIntervals=
                elapsed.toMillis()/policy.getRefillDuration().toMillis();

        if(elapsedIntervals == 0)
        {
            return;
        }

        long tokensToAdd = elapsedIntervals*policy.getRefillTokens();

        tokenBucket.setAvailableTokens(Math.min(policy.getCapacity(),tokenBucket.getAvailableTokens()+tokensToAdd));

        tokenBucket.setLastRefillTime(
                tokenBucket.getLastRefillTime().plus(policy.getRefillDuration().multipliedBy(elapsedIntervals))
        );
    }
}
