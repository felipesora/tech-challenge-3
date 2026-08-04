package com.techchallenge.user_service.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techchallenge.user_service.domain.entity.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO para resposta de dados de um usuário")
public record UsuarioResponseDTO(
        @JsonProperty("id_usuario")
        UUID id,

        String nome,

        String email,

        String cpf,

        @JsonProperty("id_tipo")
        UUID tipoUsuarioId,

        boolean ativo
) {
    public static String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }

        return cpf.replaceFirst(
                "(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
                "$1.$2.$3-$4"
        );
    }

    public static UsuarioResponseDTO fromDomain(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                formatarCpf(usuario.getCpf()),
                usuario.getTipoUsuarioId(),
                usuario.getAtivo()
        );
    }
}
