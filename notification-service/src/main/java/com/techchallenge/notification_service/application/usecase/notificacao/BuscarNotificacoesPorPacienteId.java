package com.techchallenge.notification_service.application.usecase.notificacao;

import com.techchallenge.notification_service.application.dto.NotificacaoResponseDTO;
import com.techchallenge.notification_service.application.gateway.NotificacaoGateway;

import java.util.List;
import java.util.UUID;

public class BuscarNotificacoesPorPacienteId {

    private final NotificacaoGateway gateway;

    public BuscarNotificacoesPorPacienteId(NotificacaoGateway gateway) {
        this.gateway = gateway;
    }

    public List<NotificacaoResponseDTO> executar(UUID id) {
        return gateway.buscarNotificacoesPorPacienteId(id)
                .stream()
                .map(NotificacaoResponseDTO::fromDomain)
                .toList();
    }
}
