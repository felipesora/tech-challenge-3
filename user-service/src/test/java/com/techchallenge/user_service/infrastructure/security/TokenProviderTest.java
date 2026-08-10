package com.techchallenge.user_service.infrastructure.security;

import com.techchallenge.user_service.domain.entity.TipoUsuario;
import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import com.techchallenge.user_service.domain.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenProviderTest {

    private static final String SECRET = "01234567890123456789012345678901234567890123456789";

    private final TokenProvider tokenProvider = new TokenProvider();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenProvider, "key", SECRET);
        ReflectionTestUtils.setField(tokenProvider, "expirationTime", 60_000L);
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    @Test
    void deveGerarTokenComClaimsDoUsuarioAutenticado() {
        UUID usuarioId = UUID.randomUUID();
        TipoUsuario tipoUsuario = new TipoUsuario(UUID.randomUUID(), TipoUsuarioEnum.MEDICO, true);
        Usuario usuario = new Usuario(usuarioId, "Maria", "maria@email.com", "12345678900", "hash", tipoUsuario, true);
        UserDetailsAdapter userDetails = new UserDetailsAdapter(usuario);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String token = tokenProvider.gerarToken(authentication);

        assertNotNull(token);
        Claims claims = Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(token).getPayload();
        assertEquals("maria@email.com", claims.getSubject());
        assertEquals(usuarioId.toString(), claims.get("idUsuario", String.class));
        assertEquals("MEDICO", claims.get("role", String.class));
    }

    @Test
    void deveConsiderarTokenValidoQuandoAssinaturaEValidadeCorretas() {
        String token = Jwts.builder()
                .subject("maria@email.com")
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(signingKey())
                .compact();

        assertTrue(tokenProvider.isTokenValid(token));
    }

    @Test
    void deveConsiderarTokenInvalidoQuandoExpirado() {
        String token = Jwts.builder()
                .subject("maria@email.com")
                .expiration(new Date(System.currentTimeMillis() - 60_000))
                .signWith(signingKey())
                .compact();

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
        String token = Jwts.builder()
                .subject("maria@email.com")
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(signingKey())
                .compact();

        assertEquals("maria@email.com", tokenProvider.getUsername(token));
    }
}
