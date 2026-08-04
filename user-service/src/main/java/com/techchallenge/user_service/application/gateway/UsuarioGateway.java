package com.techchallenge.user_service.application.gateway;

import com.techchallenge.user_service.domain.entity.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioGateway {
    Usuario salvar(Usuario usuario);
    List<Usuario> buscarTodos();
    Optional<Usuario> buscarPorId(UUID id);
    boolean existePorEmail(String email);
    boolean existePorCpf(String cpf);
}
