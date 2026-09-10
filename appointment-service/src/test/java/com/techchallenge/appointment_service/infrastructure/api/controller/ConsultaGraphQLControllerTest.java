package com.techchallenge.appointment_service.infrastructure.api.controller;

import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.usecase.consulta.BuscarConsultasFuturasPacienteUseCase;
import com.techchallenge.appointment_service.application.usecase.consulta.BuscarHistoricoPacienteUseCase;
import com.techchallenge.appointment_service.domain.entity.StatusConsulta;
import com.techchallenge.appointment_service.infrastructure.security.AuthenticatedUser;
import com.techchallenge.appointment_service.infrastructure.security.AuthenticationService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConsultaGraphQLControllerTest {

    private final BuscarHistoricoPacienteUseCase buscarHistoricoPacienteUseCase = mock(BuscarHistoricoPacienteUseCase.class);
    private final BuscarConsultasFuturasPacienteUseCase buscarConsultasFuturasPacienteUseCase = mock(BuscarConsultasFuturasPacienteUseCase.class);
    private final AuthenticationService authenticationService = mock(AuthenticationService.class);

    private final ConsultaGraphQLController controller = new ConsultaGraphQLController(
            buscarHistoricoPacienteUseCase,
            buscarConsultasFuturasPacienteUseCase,
            authenticationService
    );

    @Test
    void deveBuscarHistoricoPaciente() {
        UUID pacienteId = UUID.randomUUID();
        AuthenticatedUser usuario = new AuthenticatedUser(pacienteId, "paciente@email.com", "PACIENTE");
        ConsultaResponseDTO response = new ConsultaResponseDTO(
                UUID.randomUUID(),
                pacienteId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 10, 9, 0),
                StatusConsulta.REALIZADA,
                "Histórico",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(authenticationService.getAuthenticatedUser()).thenReturn(usuario);
        when(buscarHistoricoPacienteUseCase.executar(pacienteId, usuario)).thenReturn(List.of(response));

        List<ConsultaResponseDTO> resultado = controller.historicoPaciente(pacienteId);

        assertEquals(1, resultado.size());
        assertEquals(response.id(), resultado.get(0).id());
        verify(authenticationService).getAuthenticatedUser();
        verify(buscarHistoricoPacienteUseCase).executar(pacienteId, usuario);
    }

    @Test
    void deveBuscarConsultasFuturasPaciente() {
        UUID pacienteId = UUID.randomUUID();
        AuthenticatedUser usuario = new AuthenticatedUser(pacienteId, "paciente@email.com", "PACIENTE");
        ConsultaResponseDTO response = new ConsultaResponseDTO(
                UUID.randomUUID(),
                pacienteId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 25, 10, 0),
                StatusConsulta.AGENDADA,
                "Futura",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(authenticationService.getAuthenticatedUser()).thenReturn(usuario);
        when(buscarConsultasFuturasPacienteUseCase.executar(pacienteId, usuario)).thenReturn(List.of(response));

        List<ConsultaResponseDTO> resultado = controller.consultasFuturasPaciente(pacienteId);

        assertEquals(1, resultado.size());
        assertEquals(response.id(), resultado.get(0).id());
        verify(authenticationService).getAuthenticatedUser();
        verify(buscarConsultasFuturasPacienteUseCase).executar(pacienteId, usuario);
    }
}