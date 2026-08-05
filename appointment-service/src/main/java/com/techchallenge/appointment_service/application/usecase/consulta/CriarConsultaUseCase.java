package com.techchallenge.appointment_service.application.usecase.consulta;

import com.techchallenge.appointment_service.application.dto.ConsultaRequestDTO;
import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.gateway.ConsultaGateway;
import com.techchallenge.appointment_service.domain.entity.Consulta;

import java.time.LocalDateTime;

public class CriarConsultaUseCase {

    private final ConsultaGateway gateway;

    public CriarConsultaUseCase(ConsultaGateway gateway) {
        this.gateway = gateway;
    }

    public ConsultaResponseDTO executar(ConsultaRequestDTO dto) {
        Consulta consulta = new Consulta(
                null,
                dto.pacienteId(),
                dto.medicoId(),
                dto.enfermeiroId(),
                dto.dataHora(),
                dto.status(),
                dto.observacoes(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return ConsultaResponseDTO.fromDomain(gateway.salvar(consulta));
    }
}
