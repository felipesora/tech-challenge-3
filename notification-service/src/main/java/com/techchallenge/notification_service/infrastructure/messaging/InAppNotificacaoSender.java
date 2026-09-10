package com.techchallenge.notification_service.infrastructure.messaging;

import com.techchallenge.notification_service.application.gateway.NotificacaoSender;
import com.techchallenge.notification_service.domain.entity.Notificacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InAppNotificacaoSender implements NotificacaoSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(InAppNotificacaoSender.class);

    @Override
    public void enviar(Notificacao notificacao) {
        LOGGER.info(
                "Notificação in-app disponibilizada ao paciente {}. Consulta: {}, data/hora: {}, tipo: {}",
                notificacao.getPacienteId(),
                notificacao.getConsultaId(),
                notificacao.getDataHoraConsulta(),
                notificacao.getTipo()
        );
    }
}