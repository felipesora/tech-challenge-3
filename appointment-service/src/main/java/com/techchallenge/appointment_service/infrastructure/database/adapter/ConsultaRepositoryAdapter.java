package com.techchallenge.appointment_service.infrastructure.database.adapter;

import com.techchallenge.appointment_service.application.gateway.ConsultaGateway;
import com.techchallenge.appointment_service.domain.entity.Consulta;
import com.techchallenge.appointment_service.infrastructure.database.entity.ConsultaEntity;
import com.techchallenge.appointment_service.infrastructure.database.repository.ConsultaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ConsultaRepositoryAdapter implements ConsultaGateway {

    private final ConsultaRepository repository;

    public ConsultaRepositoryAdapter(ConsultaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Consulta salvar(Consulta consulta) {
        ConsultaEntity entity = toEntity(consulta);
        ConsultaEntity salvo = repository.save(entity);
        return toDomain(salvo);
    }

    @Override
    public List<Consulta> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existeConsultaMedicoNoHorario(UUID medicoId, LocalDateTime dataHora) {
        return repository.existsByMedicoIdAndDataHora(medicoId, dataHora);
    }

    private ConsultaEntity toEntity(Consulta domain) {
        ConsultaEntity entity = new ConsultaEntity();
        entity.setId(domain.getId());
        entity.setPacienteId(domain.getPacienteId());
        entity.setMedicoId(domain.getMedicoId());
        entity.setEnfermeiroId(domain.getEnfermeiroId());
        entity.setDataHora(domain.getDataHora());
        entity.setStatus(domain.getStatus());
        entity.setObservacoes(domain.getObservacoes());
        entity.setCriadoEm(domain.getCriadoEm());
        entity.setAtualizadoEm(domain.getAtualizadoEm());
        return entity;
    }

    private Consulta toDomain(ConsultaEntity entity) {
        return new Consulta(
                entity.getId(),
                entity.getPacienteId(),
                entity.getMedicoId(),
                entity.getEnfermeiroId(),
                entity.getDataHora(),
                entity.getStatus(),
                entity.getObservacoes(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}
