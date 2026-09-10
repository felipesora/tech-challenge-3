package com.techchallenge.notification_service.infrastructure.messaging;

import com.techchallenge.notification_service.domain.entity.Notificacao;
import com.techchallenge.notification_service.domain.entity.StatusNotificacao;
import com.techchallenge.notification_service.domain.entity.TipoNotificacao;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class InAppNotificacaoSenderTest {

    @Test
    void deveDisponibilizarNotificacaoSemErro() {
        InAppNotificacaoSender sender = new InAppNotificacaoSender();
        Notificacao notificacao = new Notificacao(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 20, 10, 0),
                TipoNotificacao.LEMBRETE,
                StatusNotificacao.PENDENTE,
                null,
                LocalDateTime.now()
        );

        assertDoesNotThrow(() -> sender.enviar(notificacao));
    }
}