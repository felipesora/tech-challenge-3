package com.techchallenge.appointment_service.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomAuthenticationEntryPointTest {

    private final CustomAuthenticationEntryPoint entryPoint =
            new CustomAuthenticationEntryPoint(new ObjectMapper().registerModule(new JavaTimeModule()));

    @Test
    void deveResponderComStatus401EMensagemPadrao() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException exception = new BadCredentialsException("Credenciais inválidas");

        entryPoint.commence(request, response, exception);

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentType().startsWith("application/json"));
        assertTrue(response.getContentAsString().contains("Usuário não autenticado"));
    }
}
