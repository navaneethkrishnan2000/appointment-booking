package com.thomasvallen.appointmentbooking.config.security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final Map<String, Instant> blacklistedTokens = new ConcurrentHashMap<>();

    /**
     * Blacklist a token until its expiry time
     */
    public void blacklist(String token, Instant expiry) {
        blacklistedTokens.put(token, expiry);
    }

    /**
     * Check if token is blacklisted
     */
    public boolean isTokenBlacklisted(String token) {
        Instant expiry = blacklistedTokens.get(token);
        if (expiry == null) return false;
        if (expiry.isBefore(Instant.now())) {
            blacklistedTokens.remove(token); // Clean ip expired entries
            return false;
        }
        return true;
    }

    private void cleanupExpiredTokens() {
        Instant now = Instant.now();
        blacklistedTokens.entrySet()
                .removeIf(entry -> entry.getValue().isBefore(now));
    }
}
