package com.ogbuilds.distributedratelimiter.filter;

import com.ogbuilds.distributedratelimiter.service.RateLimiterService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class RedisRateLimitingFilter implements GlobalFilter {

    private final RateLimiterService rateLimiterService;

    @Override
    public Mono<Void> filter(@NotNull ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("===== FILTER EXECUTED =====");

        String clientId = exchange.getRequest().getHeaders().getFirst("X-Client-Id");

        if (clientId == null || clientId.isBlank()) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

            return exchange.getResponse().setComplete();
        }

        return rateLimiterService
                .tryConsume(clientId)
                .flatMap(allowed -> {
                    if (allowed) {
                        return chain.filter(exchange);
                    }

                    exchange.getResponse()
                            .setStatusCode(HttpStatus.TOO_MANY_REQUESTS);

                    return exchange.getResponse().setComplete();
                });
    }
}
