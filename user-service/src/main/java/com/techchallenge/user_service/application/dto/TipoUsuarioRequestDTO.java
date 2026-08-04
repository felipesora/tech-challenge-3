package com.techchallenge.user_service.application.dto;

import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para solicitar a criação ou atualização de um tipo de usuário")
public record TipoUsuarioRequestDTO(
        @NotNull(message = "Tipo é obrigatório")
        TipoUsuarioEnum tipo
) {
}
