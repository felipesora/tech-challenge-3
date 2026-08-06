package com.techchallenge.user_service.infrastructure.database.adapter;

import com.techchallenge.user_service.application.gateway.TipoUsuarioGateway;
import com.techchallenge.user_service.domain.entity.TipoUsuario;
import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import com.techchallenge.user_service.infrastructure.database.entity.TipoUsuarioEntity;
import com.techchallenge.user_service.infrastructure.database.repository.TipoUsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TipoUsuarioRepositoryAdapter implements TipoUsuarioGateway {

    private final TipoUsuarioRepository repository;

    public TipoUsuarioRepositoryAdapter(TipoUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public TipoUsuario salvar(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity entity = toEntity(tipoUsuario);
        TipoUsuarioEntity salvo = repository.save(entity);
        return toDomain(salvo);
    }

    @Override
    public List<TipoUsuario> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<TipoUsuario> buscarPorId(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existePorTipo(TipoUsuarioEnum tipo) {
        return repository.existsByTipo(tipo);
    }

    private TipoUsuarioEntity toEntity(TipoUsuario domain) {
        TipoUsuarioEntity entity = new TipoUsuarioEntity();
        entity.setId(domain.getId());
        entity.setTipo(domain.getTipo());
        entity.setAtivo(domain.getAtivo());
        return entity;
    }

    private TipoUsuario toDomain(TipoUsuarioEntity entity) {
        return new TipoUsuario(
                entity.getId(),
                entity.getTipo(),
                entity.getAtivo()
        );
    }
}
