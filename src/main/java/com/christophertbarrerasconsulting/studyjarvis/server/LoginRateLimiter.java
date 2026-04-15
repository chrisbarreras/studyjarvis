package com.christophertbarrerasconsulting.studyjarvis.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LoginRateLimiter {
    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_MS = 5 * 60 * 1000L;
    private static final LoginRateLimiter INSTANCE = new LoginRateLimiter();

    public static LoginRateLimiter getInstance() {
        return INSTANCE;
    }

    private static final class Entry {
        int attempts;
        long windowStart;
    }

    private final ConcurrentMap<String, Entry> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String username) {
        if (username == null) return false;
        Entry e = attempts.get(username);
        if (e == null) return false;
        if (System.currentTimeMillis() - e.windowStart > WINDOW_MS) return false;
        return e.attempts >= MAX_ATTEMPTS;
    }

    public void recordFailure(String username) {
        if (username == null) return;
        long now = System.currentTimeMillis();
        attempts.compute(username, (k, e) -> {
            if (e == null || now - e.windowStart > WINDOW_MS) {
                Entry fresh = new Entry();
                fresh.windowStart = now;
                fresh.attempts = 1;
                return fresh;
            }
            e.attempts++;
            return e;
        });
    }

    public void recordSuccess(String username) {
        if (username == null) return;
        attempts.remove(username);
    }
}
