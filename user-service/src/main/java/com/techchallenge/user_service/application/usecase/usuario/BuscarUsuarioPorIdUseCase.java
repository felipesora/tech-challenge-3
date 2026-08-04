package com.techchallenge.user_service.application.usecase.usuario;

import com.techchallenge.user_service.application.dto.UsuarioResponseDTO;
import com.techchallenge.user_service.application.gateway.UsuarioGateway;
import com.techchallenge.user_service.infrastructure.exception.EntityNotFoundException;

import java.util.UUID;

public class BuscarUsuarioPorIdUseCase {

    private final UsuarioGateway usuarioGateway;

    public BuscarUsuarioPorIdUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public UsuarioResponseDTO executar(UUID id) {
        return usuarioGateway.buscarPorId(id).map(UsuarioResponseDTO::fromDomain)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com id: " + id + " não encontrado."));
    }
}
