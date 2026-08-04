package com.techchallenge.user_service.application.usecase.usuario;

import com.techchallenge.user_service.application.dto.TipoUsuarioRequestDTO;
import com.techchallenge.user_service.application.dto.TipoUsuarioResponseDTO;
import com.techchallenge.user_service.application.gateway.TipoUsuarioGateway;
import com.techchallenge.user_service.domain.entity.TipoUsuario;

import java.util.UUID;

public class CriarTipoUsuarioUseCase {

    private final TipoUsuarioGateway gateway;

    public CriarTipoUsuarioUseCase(TipoUsuarioGateway gateway) {
        this.gateway = gateway;
    }

    public TipoUsuarioResponseDTO executar(TipoUsuarioRequestDTO dto) {
        TipoUsuario tipoUsuario = new TipoUsuario(UUID.randomUUID(), dto.tipo(), true);
        return TipoUsuarioResponseDTO.fromDomain(gateway.salvar(tipoUsuario));
    }
}
