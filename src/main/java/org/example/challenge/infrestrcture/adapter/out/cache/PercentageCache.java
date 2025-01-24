package org.example.challenge.infrestrcture.adapter.out.cache;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class PercentageCache {

    private static class CacheEntry {
        double percentage;
        Instant expiryTime;

        CacheEntry(double percentage, Instant expiryTime) {
            this.percentage = percentage;
            this.expiryTime = expiryTime;
        }
    }

    private final AtomicReference<CacheEntry> cache = new AtomicReference<>();

    public void storePercentage(double percentage) {
        cache.set(new CacheEntry(percentage, Instant.now().plusSeconds(30 * 60))); // 30 minutos
    }

    public Optional<Double> getPercentage() {
        CacheEntry entry = cache.get();
        if (entry != null && Instant.now().isBefore(entry.expiryTime)) {
            return Optional.of(entry.percentage);
        }
        return Optional.empty();
    }

    public boolean hasValidPercentage() {
        return getPercentage().isPresent();
    }
}
