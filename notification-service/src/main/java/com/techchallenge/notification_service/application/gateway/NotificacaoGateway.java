package com.techchallenge.notification_service.application.gateway;

import com.techchallenge.notification_service.domain.entity.Notificacao;

import java.util.List;

public interface NotificacaoGateway {
    List<Notificacao> buscarTodos();
}
