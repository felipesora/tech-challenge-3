package com.techchallenge.appointment_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techchallenge.appointment_service.domain.entity.StatusConsulta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO para solicitar a criação ou atualização de uma consulta")
public record ConsultaRequestDTO(

        @NotNull(message = "Id do paciente é obrigatório")
        @JsonProperty("id_paciente")
        UUID pacienteId,

        @NotNull(message = "Id do médico é obrigatório")
        @JsonProperty("id_medico")
        UUID medicoId,

        @JsonProperty("id_enfermeiro")
        UUID enfermeiroId,

        @NotNull(message = "Data e Hora é obrigatório")
        @JsonProperty("data_hora")
        LocalDateTime dataHora,

        @NotNull(message = "Status da consulta é obrigatório")
        StatusConsulta status,

        String observacoes
) {
}
