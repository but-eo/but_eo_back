package org.example.but_eo.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VerificationStore {

    private static final long EXPIRATION_TIME_MILLIS = 3 * 60 * 1000; // 3분

    private final Map<String, TimedCode> store = new ConcurrentHashMap<>();
    private final Map<String, Boolean> verified = new ConcurrentHashMap<>();

    public void save(String email, String code) {
        store.put(email, new TimedCode(code, EXPIRATION_TIME_MILLIS));
    }

    public boolean verify(String email, String code) {
        TimedCode timedCode = store.get(email);
        if (timedCode == null || timedCode.isExpired()) {
            store.remove(email);
            return false;
        }

        boolean success = code.equals(timedCode.getCode());
        if (success) {
            verified.put(email, true);
            store.remove(email);
        }

        return success;
    }

    public boolean isVerified(String email) {
        return verified.getOrDefault(email, false);
    }

    public void remove(String email) {
        store.remove(email);
        verified.remove(email);
    }

    private static class TimedCode {
        private final String code;
        private final long expiryTimeMillis;

        public TimedCode(String code, long ttlMillis) {
            this.code = code;
            this.expiryTimeMillis = System.currentTimeMillis() + ttlMillis;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTimeMillis;
        }

        public String getCode() {
            return code;
        }
    }
}
