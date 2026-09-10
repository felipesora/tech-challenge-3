package com.techchallenge.notification_service.application.usecase.notificacao;

import com.techchallenge.notification_service.application.gateway.NotificacaoGateway;
import com.techchallenge.notification_service.application.gateway.NotificacaoSender;
import com.techchallenge.notification_service.domain.entity.Notificacao;
import com.techchallenge.notification_service.domain.entity.StatusNotificacao;
import com.techchallenge.notification_service.domain.entity.TipoNotificacao;
import com.techchallenge.notification_service.infrastructure.messaging.NotificationEventDTO;

import java.time.LocalDateTime;

public class CriarNotificacaoUseCase {

    private final NotificacaoGateway gateway;
    private final NotificacaoSender sender;

    public CriarNotificacaoUseCase(NotificacaoGateway gateway, NotificacaoSender sender) {
        this.gateway = gateway;
        this.sender = sender;
    }

    public void executar(NotificationEventDTO evento) {
        Notificacao notificacao = new Notificacao(
                null,
                evento.consultaId(),
                evento.pacienteId(),
                evento.dataHoraConsulta(),
                TipoNotificacao.valueOf(evento.tipoNotificacao()),
                StatusNotificacao.PENDENTE,
                null,
                LocalDateTime.now()
        );

        notificacao = gateway.salvar(notificacao);

        try {
            sender.enviar(notificacao);
            notificacao.setStatus(StatusNotificacao.ENVIADA);
            notificacao.setEnviadoEm(LocalDateTime.now());
        } catch (RuntimeException exception) {
            notificacao.setStatus(StatusNotificacao.ERRO);
        }

        gateway.salvar(notificacao);
    }
}