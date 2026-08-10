package com.techchallenge.appointment_service.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenProviderTest {

    private static final String SECRET = "01234567890123456789012345678901234567890123456789";

    private final TokenProvider tokenProvider = new TokenProvider();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenProvider, "key", SECRET);
    }

    private String gerarToken(String username, UUID idUsuario, String role, Date expiration) {
        SecretKey signingKey = Keys.hmacShaKeyFor(SECRET.getBytes());
        return Jwts.builder()
                .subject(username)
                .claim("idUsuario", idUsuario.toString())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(signingKey)
                .compact();
    }

    @Test
    void deveConsiderarTokenValidoQuandoAssinaturaEValidadeCorretas() {
        String token = gerarToken("maria@email.com", UUID.randomUUID(), "PACIENTE", new Date(System.currentTimeMillis() + 60_000));

        assertTrue(tokenProvider.isTokenValid(token));
    }

    @Test
    void deveConsiderarTokenInvalidoQuandoExpirado() {
        String token = gerarToken("maria@email.com", UUID.randomUUID(), "PACIENTE", new Date(System.currentTimeMillis() - 60_000));

        assertFalse(tokenProvider.isTokenValid(token));
    }

    @Test
    void deveConsiderarTokenInvalidoQuandoAssinaturaDivergente() {
        SecretKey outraChave = Keys.hmacShaKeyFor("outra-chave-completamente-diferente-0123456789".getBytes());
        String token = Jwts.builder()
                .subject("maria@email.com")
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(outraChave)
                .compact();

        assertFalse(tokenProvider.isTokenValid(token));
    }

    @Test
    void deveExtrairUsername() {
        String token = gerarToken("maria@email.com", UUID.randomUUID(), "PACIENTE", new Date(System.currentTimeMillis() + 60_000));

        assertEquals("maria@email.com", tokenProvider.getUsername(token));
    }

    @Test
    void deveExtrairIdUsuario() {
        UUID idUsuario = UUID.randomUUID();
        String token = gerarToken("maria@email.com", idUsuario, "PACIENTE", new Date(System.currentTimeMillis() + 60_000));

        assertEquals(idUsuario, tokenProvider.getIdUsuario(token));
    }

    @Test
    void deveExtrairRole() {
        String token = gerarToken("maria@email.com", UUID.randomUUID(), "MEDICO", new Date(System.currentTimeMillis() + 60_000));

        assertEquals("MEDICO", tokenProvider.getRole(token));
    }
}
