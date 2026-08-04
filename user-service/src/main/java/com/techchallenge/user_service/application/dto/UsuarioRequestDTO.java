package com.techchallenge.user_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Schema(description = "DTO para solicitar a criação ou atualização de um usuário")
public record UsuarioRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "E-mail no formato inválido")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
        String email,

        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(
                regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$",
                message = "O CPF deve estar no formato 123.456.789-00"
        )
        String cpf,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, max = 150, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha,

        @NotNull(message = "Id do tipo de usuário é obrigatório")
        @JsonProperty("id_tipo")
        UUID tipoUsuarioId
) {
}
