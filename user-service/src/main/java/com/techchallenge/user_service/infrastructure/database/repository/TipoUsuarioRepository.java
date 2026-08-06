package com.techchallenge.user_service.infrastructure.database.repository;

import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import com.techchallenge.user_service.infrastructure.database.entity.TipoUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuarioEntity, UUID> {

    boolean existsByTipo(TipoUsuarioEnum tipo);
}
