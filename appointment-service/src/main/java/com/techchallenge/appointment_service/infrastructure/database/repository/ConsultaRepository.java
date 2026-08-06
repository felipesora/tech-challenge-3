package com.techchallenge.appointment_service.infrastructure.database.repository;

import com.techchallenge.appointment_service.infrastructure.database.entity.ConsultaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaEntity, UUID> {

    boolean existsByMedicoIdAndDataHora(UUID medicoId, LocalDateTime dataHora);
}
