package com.techchallenge.user_service.infrastructure.database.repository;

import com.techchallenge.user_service.infrastructure.database.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
