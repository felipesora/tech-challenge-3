package com.techchallenge.appointment_service.application.usecase.consulta;

import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.gateway.ConsultaGateway;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BuscarConsultasFuturasPacienteUseCase {
    private final ConsultaGateway gateway;

    public BuscarConsultasFuturasPacienteUseCase(ConsultaGateway gateway) {
        this.gateway = gateway;
    }

    public List<ConsultaResponseDTO> executar(UUID pacienteId) {
        return gateway.buscarConsultasFuturasPorPacienteId(pacienteId, LocalDateTime.now()).stream().map(ConsultaResponseDTO::fromDomain).toList();
    }
}