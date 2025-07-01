package br.com.prf.leitordeplacas.Utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtBlacklist {
    private final ConcurrentHashMap<String, Instant> invalidatedTokens = new ConcurrentHashMap<>();

    public void invalidateToken(String token, Instant expirationTime) {
        invalidatedTokens.put(token, expirationTime);
    }

    public boolean isTokenInvalidated(String token) {
        invalidatedTokens.entrySet().removeIf(entry -> entry.getValue().isBefore(Instant.now()));
        return invalidatedTokens.containsKey(token);
    }
}
