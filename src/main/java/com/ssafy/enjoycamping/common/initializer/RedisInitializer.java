package com.ssafy.enjoycamping.common.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisInitializer {
    private final StringRedisTemplate redisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeRedisConnection() {
        try {
            redisTemplate.opsForValue().set("connectionTestKey", "test", Duration.ofSeconds(1));
            redisTemplate.delete("connectionTestKey");
        } catch (Exception e) {
            log.error("Redis initialization failed: {}", e.getMessage());
        }
    }
}
