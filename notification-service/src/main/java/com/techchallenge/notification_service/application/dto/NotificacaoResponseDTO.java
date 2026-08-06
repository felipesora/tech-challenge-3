package com.techchallenge.notification_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techchallenge.notification_service.domain.entity.Notificacao;
import com.techchallenge.notification_service.domain.entity.StatusNotificacao;
import com.techchallenge.notification_service.domain.entity.TipoNotificacao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO para resposta de dados de uma notificação")
public record NotificacaoResponseDTO(
        @JsonProperty("id_notificacao")
        UUID id,

        @JsonProperty("id_consulta")
        UUID consultaId,

        @JsonProperty("id_paciente")
        UUID pacienteId,

        @JsonProperty("data_hora_consulta")
        LocalDateTime dataHoraConsulta,

        TipoNotificacao tipo,

        @JsonProperty("status_notificacao")
        StatusNotificacao status,

        @JsonProperty("enviado_em")
        LocalDateTime enviadoEm,

        @JsonProperty("criado_em")
        LocalDateTime criadoEm
) {
    public static NotificacaoResponseDTO fromDomain(Notificacao notificacao) {
        return new NotificacaoResponseDTO(
                notificacao.getId(),
                notificacao.getConsultaId(),
                notificacao.getPacienteId(),
                notificacao.getDataHoraConsulta(),
                notificacao.getTipo(),
                notificacao.getStatus(),
                notificacao.getEnviadoEm(),
                notificacao.getCriadoEm()
        );
    }
}
