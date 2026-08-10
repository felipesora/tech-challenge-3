package com.techchallenge.appointment_service.application.usecase.consulta;

import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.gateway.ConsultaGateway;
import java.util.List;
import java.util.UUID;

public class BuscarHistoricoPacienteUseCase {
    private final ConsultaGateway gateway;

    public BuscarHistoricoPacienteUseCase(ConsultaGateway gateway) {
        this.gateway = gateway;
    }

    public List<ConsultaResponseDTO> executar(UUID pacienteId) {
        return gateway.buscarPorPacienteId(pacienteId).stream().map(ConsultaResponseDTO::fromDomain).toList();
    }
}