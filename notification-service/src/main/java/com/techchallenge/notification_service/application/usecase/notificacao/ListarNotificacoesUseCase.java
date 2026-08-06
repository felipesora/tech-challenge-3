package com.techchallenge.notification_service.application.usecase.notificacao;

import com.techchallenge.notification_service.application.dto.NotificacaoResponseDTO;
import com.techchallenge.notification_service.application.gateway.NotificacaoGateway;

import java.util.List;

public class ListarNotificacoesUseCase {

    private final NotificacaoGateway gateway;

    public ListarNotificacoesUseCase(NotificacaoGateway gateway) {
        this.gateway = gateway;
    }

    public List<NotificacaoResponseDTO> executar() {
        return gateway.buscarTodos()
                .stream()
                .map(NotificacaoResponseDTO::fromDomain)
                .toList();
    }
}
