package com.techchallenge.user_service.application.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank
        String email,

        @NotBlank
        String senha
) {
}
