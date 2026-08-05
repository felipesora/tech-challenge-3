package com.techchallenge.appointment_service.application.usecase.consulta;

import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.gateway.ConsultaGateway;

import java.util.List;

public class ListarConsultasUseCase {

    private final ConsultaGateway gateway;

    public ListarConsultasUseCase(ConsultaGateway gateway) {
        this.gateway = gateway;
    }

    public List<ConsultaResponseDTO> executar() {
        return gateway.buscarTodos()
                .stream()
                .map(ConsultaResponseDTO::fromDomain)
                .toList();
    }
}
