package com.ogbuilds.distributedratelimiter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rate-limiter")
public class RateLimiterProperties {

    private long capacity;

    private long refillTokens;

    private Duration refillDuration;

    private String redisPrefix;

}
