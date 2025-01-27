package org.example.challenge.infraestrcture.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class PercentageCache {

    private static final String CACHE_KEY = "percentage";
    private static final long CACHE_EXPIRY = 30; // 30 minutes

    @Autowired
    private RedisTemplate<String, Double> redisTemplate;

    public void storePercentage(double percentage) {
        redisTemplate.opsForValue().set(CACHE_KEY, percentage, CACHE_EXPIRY, TimeUnit.MINUTES);
    }

    public Optional<Double> getPercentage() {
        Double percentage = redisTemplate.opsForValue().get(CACHE_KEY);
        return Optional.ofNullable(percentage);
    }
}
