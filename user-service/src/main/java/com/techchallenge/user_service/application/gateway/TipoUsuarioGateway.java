package com.techchallenge.user_service.application.gateway;

import com.techchallenge.user_service.domain.entity.TipoUsuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TipoUsuarioGateway {
    TipoUsuario salvar(TipoUsuario tipoUsuario);
    List<TipoUsuario> buscarTodos();
    Optional<TipoUsuario> buscarPorId(UUID id);
}
