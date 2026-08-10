package com.techchallenge.notification_service.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtAuthenticationFilterTest {

    private final TokenProvider tokenProvider = mock(TokenProvider.class);
    private final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(tokenProvider);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain filterChain = mock(FilterChain.class);

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveAutenticarQuandoTokenForValido() throws Exception {
        UUID idUsuario = UUID.randomUUID();
        when(request.getHeader("Authorization")).thenReturn("Bearer token-valido");
        when(tokenProvider.isTokenValid("token-valido")).thenReturn(true);
        when(tokenProvider.getIdUsuario("token-valido")).thenReturn(idUsuario);
        when(tokenProvider.getUsername("token-valido")).thenReturn("maria@email.com");
        when(tokenProvider.getRole("token-valido")).thenReturn("PACIENTE");

        filter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
        AuthenticatedUser principal = (AuthenticatedUser) authentication.getPrincipal();
        assertEquals(idUsuario, principal.id());
        assertEquals("maria@email.com", principal.email());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE")));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void naoDeveAutenticarQuandoTokenForInvalido() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer token-invalido");
        when(tokenProvider.isTokenValid("token-invalido")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void naoDeveAutenticarQuandoHeaderNaoForBearer() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic abc123");

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void naoDeveAutenticarQuandoHeaderForNulo() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void naoDeveReautenticarQuandoJaExistirAutenticacao() throws Exception {
        AuthenticatedUser principalExistente = new AuthenticatedUser(UUID.randomUUID(), "existente@email.com", "MEDICO");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principalExistente, null)
        );
        when(request.getHeader("Authorization")).thenReturn("Bearer token-valido");
        when(tokenProvider.isTokenValid("token-valido")).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertEquals(principalExistente, authentication.getPrincipal());
        verify(tokenProvider, never()).getIdUsuario("token-valido");
        verify(filterChain).doFilter(request, response);
    }
}
