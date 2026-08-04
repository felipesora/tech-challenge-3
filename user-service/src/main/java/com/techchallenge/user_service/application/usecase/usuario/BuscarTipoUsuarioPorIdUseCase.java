package com.techchallenge.user_service.application.usecase.usuario;

import com.techchallenge.user_service.application.dto.TipoUsuarioResponseDTO;
import com.techchallenge.user_service.application.gateway.TipoUsuarioGateway;
import com.techchallenge.user_service.infrastructure.exception.EntityNotFoundException;

import java.util.UUID;

public class BuscarTipoUsuarioPorIdUseCase {

    private final TipoUsuarioGateway gateway;

    public BuscarTipoUsuarioPorIdUseCase(TipoUsuarioGateway gateway) {
        this.gateway = gateway;
    }

    public TipoUsuarioResponseDTO executar(UUID id) {
        return gateway.buscarPorId(id).map(TipoUsuarioResponseDTO::fromDomain)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de usuário com id: " + id + " não encontrado."));
    }
}
