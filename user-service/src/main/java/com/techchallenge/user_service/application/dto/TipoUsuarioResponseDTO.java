package com.techchallenge.user_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techchallenge.user_service.domain.entity.TipoUsuario;
import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO para resposta de dados de um tipo de usuário")
public record TipoUsuarioResponseDTO(
        @JsonProperty("id_tipo_usuario")
        UUID id,
        TipoUsuarioEnum tipo,
        boolean ativo) {

    public static TipoUsuarioResponseDTO fromDomain(TipoUsuario tipoUsuario) {
        return new TipoUsuarioResponseDTO(tipoUsuario.getId(), tipoUsuario.getTipo(), tipoUsuario.getAtivo());
    }
}
