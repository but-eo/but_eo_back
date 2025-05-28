package org.example.but_eo.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VerificationStore {

    private final Map<String, String> store = new ConcurrentHashMap<>();
    private final Map<String, Boolean> verified = new ConcurrentHashMap<>();

    public void save(String email, String code) {
        store.put(email, code);
    }

    public boolean verify(String email, String code) {
        boolean success = code.equals(store.get(email));
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
}