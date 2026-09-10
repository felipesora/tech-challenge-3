package com.techchallenge.notification_service.application.gateway;

import com.techchallenge.notification_service.domain.entity.Notificacao;

public interface NotificacaoSender {

    void enviar(Notificacao notificacao);
}