package com.techchallenge.appointment_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techchallenge.appointment_service.domain.entity.Consulta;
import com.techchallenge.appointment_service.domain.entity.StatusConsulta;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO para resposta de dados de uma consulta")
public record ConsultaResponseDTO(
        @JsonProperty("id_consulta")
        UUID id,

        @JsonProperty("id_paciente")
        UUID pacienteId,

        @JsonProperty("id_medico")
        UUID medicoId,

        @JsonProperty("id_enfermeiro")
        UUID enfermeiroId,

        @JsonProperty("data_hora")
        LocalDateTime dataHora,

        StatusConsulta status,

        String observacoes,

        @JsonProperty("criado_em")
        LocalDateTime criadoEm,

        @JsonProperty("atualizado_em")
        LocalDateTime atualizadoEm
) {
    public static ConsultaResponseDTO fromDomain(Consulta consulta) {
        return new ConsultaResponseDTO(
                consulta.getId(),
                consulta.getPacienteId(),
                consulta.getMedicoId(),
                consulta.getEnfermeiroId(),
                consulta.getDataHora(),
                consulta.getStatus(),
                consulta.getObservacoes(),
                consulta.getCriadoEm(),
                consulta.getAtualizadoEm()
        );
    }
}
