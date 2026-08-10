package com.techchallenge.notification_service.infrastructure.messaging;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationEventDTO(
        UUID consultaId,
        UUID pacienteId,
        LocalDateTime dataHoraConsulta,
        String tipoNotificacao
) {
}