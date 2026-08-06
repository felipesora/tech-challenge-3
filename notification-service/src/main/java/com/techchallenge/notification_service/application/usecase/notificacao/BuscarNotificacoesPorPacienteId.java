package com.techchallenge.notification_service.application.usecase.notificacao;

import com.techchallenge.notification_service.application.dto.NotificacaoResponseDTO;
import com.techchallenge.notification_service.application.gateway.NotificacaoGateway;
import com.techchallenge.notification_service.infrastructure.security.AuthenticatedUser;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.UUID;

public class BuscarNotificacoesPorPacienteId {

    private final NotificacaoGateway gateway;

    public BuscarNotificacoesPorPacienteId(NotificacaoGateway gateway) {
        this.gateway = gateway;
    }

    public List<NotificacaoResponseDTO> executar(UUID id, AuthenticatedUser usuario) {

        if ("PACIENTE".equals(usuario.role()) && !usuario.id().equals(id)) {
            throw new AccessDeniedException("Você não possui permissão para acessar essas notificações.");
        }

        return gateway.buscarNotificacoesPorPacienteId(id)
                .stream()
                .map(NotificacaoResponseDTO::fromDomain)
                .toList();
    }
}
