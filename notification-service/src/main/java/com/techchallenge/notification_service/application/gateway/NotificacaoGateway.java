package com.techchallenge.notification_service.application.gateway;

import com.techchallenge.notification_service.domain.entity.Notificacao;

import java.util.List;
import java.util.UUID;

public interface NotificacaoGateway {
    List<Notificacao> buscarTodos();

    List<Notificacao> buscarNotificacoesPorPacienteId(UUID id);
}
