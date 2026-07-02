package com.ogbuilds.distributedratelimiter.model;

import lombok.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBucket {

    private long availableTokens;

    private Instant lastRefillTime;

}
