package com.techchallenge.user_service.application.usecase.auth;

import com.techchallenge.user_service.application.dto.UsuarioRequestDTO;
import com.techchallenge.user_service.application.dto.UsuarioResponseDTO;
import com.techchallenge.user_service.application.gateway.TipoUsuarioGateway;
import com.techchallenge.user_service.application.gateway.UsuarioGateway;
import com.techchallenge.user_service.domain.entity.TipoUsuario;
import com.techchallenge.user_service.domain.entity.Usuario;
import com.techchallenge.user_service.infrastructure.exception.BadRequestException;
import com.techchallenge.user_service.infrastructure.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CriarUsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final PasswordEncoder passwordEncoder;

    public CriarUsuarioUseCase(UsuarioGateway usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway, PasswordEncoder passwordEncoder) {
        this.usuarioGateway = usuarioGateway;
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO executar(UsuarioRequestDTO request) {
        String cpf = request.cpf().replaceAll("[^0-9]", "");

        if (usuarioGateway.existePorEmail(request.email())) {
            throw new BadRequestException("Já existe um usuário cadastrado com este e-mail.");
        }

        if (usuarioGateway.existePorCpf(cpf)) {
            throw new BadRequestException("Já existe um usuário cadastrado com este CPF.");
        }

        TipoUsuario tipoUsuario = tipoUsuarioGateway.buscarPorId(request.tipoUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de usuário com id: " + request.tipoUsuarioId() + " não encontrado."));

        Usuario novoUsuario = new Usuario(
                null,
                request.nome(),
                request.email(),
                cpf,
                passwordEncoder.encode(request.senha()),
                tipoUsuario,
                true
        );

        Usuario usuarioSalvo = usuarioGateway.salvar(novoUsuario);
        return UsuarioResponseDTO.fromDomain(usuarioSalvo);
    }
}
