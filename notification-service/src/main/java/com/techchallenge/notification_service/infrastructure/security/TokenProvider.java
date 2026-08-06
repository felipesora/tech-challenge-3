package com.techchallenge.notification_service.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.UUID;

@Component
public class TokenProvider {

    @Value("${jwt.key}")
    private String key;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID getIdUsuario(String token) {
        String id = getClaims(token).get("idUsuario", String.class);
        return UUID.fromString(id);
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }
}
