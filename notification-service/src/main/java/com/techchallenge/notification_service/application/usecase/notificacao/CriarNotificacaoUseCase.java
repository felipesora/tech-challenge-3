package com.techchallenge.notification_service.application.usecase.notificacao;

import com.techchallenge.notification_service.application.gateway.NotificacaoGateway;
import com.techchallenge.notification_service.domain.entity.Notificacao;
import com.techchallenge.notification_service.domain.entity.StatusNotificacao;
import com.techchallenge.notification_service.domain.entity.TipoNotificacao;
import com.techchallenge.notification_service.infrastructure.messaging.NotificationEventDTO;

import java.time.LocalDateTime;

public class CriarNotificacaoUseCase {
    private final NotificacaoGateway gateway;

    public CriarNotificacaoUseCase(NotificacaoGateway gateway) {
        this.gateway = gateway;
    }

    public void executar(NotificationEventDTO evento) {
        Notificacao notificacao = new Notificacao(
                null,
                evento.consultaId(),
                evento.pacienteId(),
                evento.dataHoraConsulta(),
                TipoNotificacao.valueOf(evento.tipoNotificacao()),
                StatusNotificacao.PENDENTE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        gateway.salvar(notificacao);
    }
}