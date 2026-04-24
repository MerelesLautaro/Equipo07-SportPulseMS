package com.Equipo07_SportPulseMS.ms_notifications.service.state;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionStateService {

    private final RedisTemplate<String, Integer> redisTemplate;

    private static final String KEY_PREFIX = "subscription:";

    private String buildKey(UUID subscriptionId, Integer fixtureId) {
        return KEY_PREFIX + subscriptionId + ":fixture:" + fixtureId + ":events_count";
    }

    public int getLastEventCount(UUID subscriptionId, Integer fixtureId) {
        Integer value = redisTemplate.opsForValue().get(buildKey(subscriptionId, fixtureId));
        return value != null ? value : 0;
    }

    public void saveLastEventCount(UUID subscriptionId, Integer fixtureId, int count) {
        redisTemplate.opsForValue().set(buildKey(subscriptionId, fixtureId), count);
    }
}