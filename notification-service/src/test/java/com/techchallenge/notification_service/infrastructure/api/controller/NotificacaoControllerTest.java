package com.techchallenge.notification_service.infrastructure.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.notification_service.application.dto.NotificacaoResponseDTO;
import com.techchallenge.notification_service.application.usecase.notificacao.BuscarNotificacoesPorPacienteId;
import com.techchallenge.notification_service.application.usecase.notificacao.ListarNotificacoesUseCase;
import com.techchallenge.notification_service.domain.entity.StatusNotificacao;
import com.techchallenge.notification_service.domain.entity.TipoNotificacao;
import com.techchallenge.notification_service.infrastructure.security.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificacaoControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final ListarNotificacoesUseCase listarUseCase = mock(ListarNotificacoesUseCase.class);
    private final BuscarNotificacoesPorPacienteId buscarUseCase = mock(BuscarNotificacoesPorPacienteId.class);

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new NotificacaoController(listarUseCase, buscarUseCase))
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void deveListarNotificacoes() throws Exception {
        NotificacaoResponseDTO response = new NotificacaoResponseDTO(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.of(2026, 8, 20, 10, 0), TipoNotificacao.LEMBRETE, StatusNotificacao.PENDENTE, LocalDateTime.now(), LocalDateTime.now());
        when(listarUseCase.executar()).thenReturn(List.of(response));

        mockMvc.perform(get("/notificacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_notificacao").value(response.id().toString()));
    }

    @Test
    void deveBuscarNotificacoesPorPaciente() throws Exception {
        UUID pacienteId = UUID.randomUUID();
        NotificacaoResponseDTO response = new NotificacaoResponseDTO(UUID.randomUUID(), UUID.randomUUID(), pacienteId, LocalDateTime.of(2026, 8, 20, 10, 0), TipoNotificacao.ALTERACAO, StatusNotificacao.PENDENTE, LocalDateTime.now(), LocalDateTime.now());
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(pacienteId, "paciente@email.com", "PACIENTE");
        when(buscarUseCase.executar(any(UUID.class), any(AuthenticatedUser.class))).thenReturn(List.of(response));

        mockMvc.perform(get("/notificacoes/paciente/{id}", pacienteId)
                        .principal(new UsernamePasswordAuthenticationToken(authenticatedUser, null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_paciente").value(pacienteId.toString()));
    }
}
