package com.techchallenge.appointment_service.infrastructure.api.controller;

import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.usecase.consulta.BuscarConsultasFuturasPacienteUseCase;
import com.techchallenge.appointment_service.application.usecase.consulta.BuscarHistoricoPacienteUseCase;
import com.techchallenge.appointment_service.domain.entity.StatusConsulta;
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
    private final ConsultaGraphQLController controller = new ConsultaGraphQLController(buscarHistoricoPacienteUseCase, buscarConsultasFuturasPacienteUseCase);

    @Test
    void deveBuscarHistoricoPaciente() {
        UUID pacienteId = UUID.randomUUID();
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

        when(buscarHistoricoPacienteUseCase.executar(pacienteId)).thenReturn(List.of(response));

        List<ConsultaResponseDTO> resultado = controller.historicoPaciente(pacienteId);

        assertEquals(1, resultado.size());
        assertEquals(response.id(), resultado.get(0).id());
        verify(buscarHistoricoPacienteUseCase).executar(pacienteId);
    }

    @Test
    void deveBuscarConsultasFuturasPaciente() {
        UUID pacienteId = UUID.randomUUID();
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

        when(buscarConsultasFuturasPacienteUseCase.executar(pacienteId)).thenReturn(List.of(response));

        List<ConsultaResponseDTO> resultado = controller.consultasFuturasPaciente(pacienteId);

        assertEquals(1, resultado.size());
        assertEquals(response.id(), resultado.get(0).id());
        verify(buscarConsultasFuturasPacienteUseCase).executar(pacienteId);
    }
}
