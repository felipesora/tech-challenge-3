package com.techchallenge.user_service.application.usecase.tipoUsuario;

import com.techchallenge.user_service.application.dto.TipoUsuarioRequestDTO;
import com.techchallenge.user_service.application.dto.TipoUsuarioResponseDTO;
import com.techchallenge.user_service.application.gateway.TipoUsuarioGateway;
import com.techchallenge.user_service.domain.entity.TipoUsuario;
import com.techchallenge.user_service.infrastructure.exception.BadRequestException;

public class CriarTipoUsuarioUseCase {

    private final TipoUsuarioGateway gateway;

    public CriarTipoUsuarioUseCase(TipoUsuarioGateway gateway) {
        this.gateway = gateway;
    }

    public TipoUsuarioResponseDTO executar(TipoUsuarioRequestDTO dto) {
        if (gateway.existePorTipo(dto.tipo())) {
            throw new BadRequestException("Esse tipo de usuário já está cadastrado.");
        }
        TipoUsuario tipoUsuario = new TipoUsuario(null, dto.tipo(), true);
        return TipoUsuarioResponseDTO.fromDomain(gateway.salvar(tipoUsuario));
    }
}
