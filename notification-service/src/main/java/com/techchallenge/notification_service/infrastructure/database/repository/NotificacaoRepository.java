package com.techchallenge.notification_service.infrastructure.database.repository;

import com.techchallenge.notification_service.infrastructure.database.entity.NotificacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificacaoRepository extends JpaRepository<NotificacaoEntity, UUID> {

    List<NotificacaoEntity> findByPacienteId(UUID pacienteId);
}
