package com.techchallenge.user_service.infrastructure.database.adapter;

import com.techchallenge.user_service.application.gateway.UsuarioGateway;
import com.techchallenge.user_service.domain.entity.Usuario;
import com.techchallenge.user_service.infrastructure.database.entity.TipoUsuarioEntity;
import com.techchallenge.user_service.infrastructure.database.entity.UsuarioEntity;
import com.techchallenge.user_service.infrastructure.database.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioRepositoryAdapter implements UsuarioGateway {

    private final UsuarioRepository repository;

    public UsuarioRepositoryAdapter(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity entity = toEntity(usuario);
        UsuarioEntity salvo = repository.save(entity);
        return toDomain(salvo);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    private UsuarioEntity toEntity(Usuario domain) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEmail(domain.getEmail());
        entity.setCpf(domain.getCpf());
        entity.setSenhaHash(domain.getSenhaHash());
        entity.setAtivo(domain.getAtivo());

        if (domain.getTipoUsuarioId() != null) {
            TipoUsuarioEntity tipo = new TipoUsuarioEntity();
            tipo.setId(domain.getTipoUsuarioId());
            entity.setTipoUsuario(tipo);
        }
        return entity;
    }

    private Usuario toDomain(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getCpf(),
                entity.getSenhaHash(),
                entity.getTipoUsuario() != null ? entity.getTipoUsuario().getId() : null,
                entity.getAtivo()
        );
    }
}
