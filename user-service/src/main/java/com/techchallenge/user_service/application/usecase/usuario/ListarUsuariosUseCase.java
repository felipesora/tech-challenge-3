package com.techchallenge.user_service.application.usecase.usuario;

import com.techchallenge.user_service.application.dto.UsuarioResponseDTO;
import com.techchallenge.user_service.application.gateway.UsuarioGateway;

import java.util.List;

public class ListarUsuariosUseCase {

    private final UsuarioGateway usuarioGateway;

    public ListarUsuariosUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public List<UsuarioResponseDTO> executar() {
        return usuarioGateway.buscarTodos()
                .stream()
                .map(UsuarioResponseDTO::fromDomain)
                .toList();
    }
}
