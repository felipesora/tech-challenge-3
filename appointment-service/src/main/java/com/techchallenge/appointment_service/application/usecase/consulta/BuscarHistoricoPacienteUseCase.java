package com.techchallenge.appointment_service.application.usecase.consulta;

import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.gateway.ConsultaGateway;
import com.techchallenge.appointment_service.infrastructure.security.AuthenticatedUser;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.UUID;

public class BuscarHistoricoPacienteUseCase {
    private final ConsultaGateway gateway;

    public BuscarHistoricoPacienteUseCase(ConsultaGateway gateway) {
        this.gateway = gateway;
    }

    public List<ConsultaResponseDTO> executar(UUID pacienteId, AuthenticatedUser usuario) {
        validarAcessoAoPaciente(pacienteId, usuario);

        return gateway.buscarPorPacienteId(pacienteId)
                .stream()
                .map(ConsultaResponseDTO::fromDomain)
                .toList();
    }

    private void validarAcessoAoPaciente(UUID pacienteId, AuthenticatedUser usuario) {
        if ("PACIENTE".equals(usuario.role()) && !usuario.id().equals(pacienteId)) {
            throw new AccessDeniedException("Você não possui permissão para acessar o histórico de outro paciente.");
        }
    }
}
