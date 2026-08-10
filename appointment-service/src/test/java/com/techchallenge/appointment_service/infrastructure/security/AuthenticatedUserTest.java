package com.techchallenge.appointment_service.infrastructure.security;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthenticatedUserTest {

    @Test
    void deveArmazenarDadosDoUsuarioAutenticado() {
        UUID id = UUID.randomUUID();

        AuthenticatedUser user = new AuthenticatedUser(id, "maria@email.com", "PACIENTE");

        assertEquals(id, user.id());
        assertEquals("maria@email.com", user.email());
        assertEquals("PACIENTE", user.role());
    }
}
