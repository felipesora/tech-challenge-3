package com.techchallenge.appointment_service.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomAccessDeniedHandlerTest {

    private final CustomAccessDeniedHandler handler =
            new CustomAccessDeniedHandler(new ObjectMapper().registerModule(new JavaTimeModule()));

    @Test
    void deveResponderComStatus403EMensagemPadrao() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AccessDeniedException exception = new AccessDeniedException("Acesso negado");

        handler.handle(request, response, exception);

        assertEquals(403, response.getStatus());
        assertTrue(response.getContentType().startsWith("application/json"));
        assertTrue(response.getContentAsString().contains("Usuário sem permissão"));
    }
}
