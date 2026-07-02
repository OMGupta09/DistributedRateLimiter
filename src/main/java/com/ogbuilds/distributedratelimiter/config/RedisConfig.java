package com.ogbuilds.distributedratelimiter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogbuilds.distributedratelimiter.model.TokenBucket;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    public ReactiveRedisTemplate<String, TokenBucket> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory, ObjectMapper objectMapper
    )
    {

        Jackson2JsonRedisSerializer<TokenBucket> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, TokenBucket.class);

        RedisSerializationContext<String, TokenBucket> context=
                RedisSerializationContext
                        .<String, TokenBucket>newSerializationContext(new StringRedisSerializer())
                        .value(valueSerializer)
                        .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }

}
