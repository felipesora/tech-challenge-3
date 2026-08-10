package com.techchallenge.notification_service.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomAccessDeniedHandlerTest {

    private final CustomAccessDeniedHandler handler =
            new CustomAccessDeniedHandler(new JsonMapper());

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
