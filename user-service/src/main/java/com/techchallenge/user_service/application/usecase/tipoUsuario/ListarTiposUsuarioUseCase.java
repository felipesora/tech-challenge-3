package com.techchallenge.user_service.application.usecase.tipoUsuario;

import com.techchallenge.user_service.application.dto.TipoUsuarioResponseDTO;
import com.techchallenge.user_service.application.gateway.TipoUsuarioGateway;

import java.util.List;

public class ListarTiposUsuarioUseCase {

    private final TipoUsuarioGateway gateway;

    public ListarTiposUsuarioUseCase(TipoUsuarioGateway gateway) {
        this.gateway = gateway;
    }

    public List<TipoUsuarioResponseDTO> executar() {
        return gateway.buscarTodos()
                .stream()
                .map(TipoUsuarioResponseDTO::fromDomain)
                .toList();
    }
}
