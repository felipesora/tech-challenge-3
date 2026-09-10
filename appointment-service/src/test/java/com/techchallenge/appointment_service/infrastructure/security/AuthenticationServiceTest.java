package com.techchallenge.appointment_service.infrastructure.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticationServiceTest {

    private final AuthenticationService authenticationService = new AuthenticationService();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveRetornarUsuarioAutenticadoQuandoPrincipalForValido() {
        AuthenticatedUser principal = new AuthenticatedUser(UUID.randomUUID(), "maria@email.com", "PACIENTE");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null)
        );

        AuthenticatedUser resultado = authenticationService.getAuthenticatedUser();

        assertEquals(principal, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoNaoHouverAutenticacao() {
        SecurityContextHolder.clearContext();

        assertThrows(IllegalArgumentException.class, authenticationService::getAuthenticatedUser);
    }

    @Test
    void deveLancarExcecaoQuandoPrincipalNaoForAuthenticatedUser() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("outroPrincipal", null)
        );

        assertThrows(IllegalArgumentException.class, authenticationService::getAuthenticatedUser);
    }
}
