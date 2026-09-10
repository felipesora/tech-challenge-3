package com.techchallenge.appointment_service.application.usecase.consulta;

import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.gateway.ConsultaGateway;
import com.techchallenge.appointment_service.infrastructure.security.AuthenticatedUser;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BuscarConsultasFuturasPacienteUseCase {
    private final ConsultaGateway gateway;

    public BuscarConsultasFuturasPacienteUseCase(ConsultaGateway gateway) {
        this.gateway = gateway;
    }

    public List<ConsultaResponseDTO> executar(UUID pacienteId, AuthenticatedUser usuario) {
        validarAcessoAoPaciente(pacienteId, usuario);

        return gateway.buscarConsultasFuturasPorPacienteId(pacienteId, LocalDateTime.now())
                .stream()
                .map(ConsultaResponseDTO::fromDomain)
                .toList();
    }

    private void validarAcessoAoPaciente(UUID pacienteId, AuthenticatedUser usuario) {
        if ("PACIENTE".equals(usuario.role()) && !usuario.id().equals(pacienteId)) {
            throw new AccessDeniedException("Você não possui permissão para acessar consultas de outro paciente.");
        }
    }
}
