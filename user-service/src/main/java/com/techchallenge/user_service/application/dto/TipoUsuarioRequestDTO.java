package com.techchallenge.user_service.application.dto;

import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import jakarta.validation.constraints.NotNull;

public record TipoUsuarioRequestDTO(
        @NotNull(message = "Tipo é obrigatório")
        TipoUsuarioEnum tipo
) {
}
