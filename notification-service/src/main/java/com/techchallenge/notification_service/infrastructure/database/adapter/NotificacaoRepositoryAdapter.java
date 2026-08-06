package com.techchallenge.notification_service.infrastructure.database.adapter;

import com.techchallenge.notification_service.application.gateway.NotificacaoGateway;
import com.techchallenge.notification_service.domain.entity.Notificacao;
import com.techchallenge.notification_service.infrastructure.database.entity.NotificacaoEntity;
import com.techchallenge.notification_service.infrastructure.database.repository.NotificacaoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class NotificacaoRepositoryAdapter implements NotificacaoGateway {

    private final NotificacaoRepository repository;

    public NotificacaoRepositoryAdapter(NotificacaoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Notificacao> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Notificacao> buscarNotificacoesPorPacienteId(UUID id) {
        return repository.findByPacienteId(id)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private NotificacaoEntity toEntity(Notificacao domain) {
        NotificacaoEntity entity = new NotificacaoEntity();
        entity.setId(domain.getId());
        entity.setConsultaId(domain.getConsultaId());
        entity.setPacienteId(domain.getPacienteId());
        entity.setDataHoraConsulta(domain.getDataHoraConsulta());
        entity.setTipo(domain.getTipo());
        entity.setStatus(domain.getStatus());
        entity.setEnviadoEm(domain.getEnviadoEm());
        entity.setCriadoEm(domain.getCriadoEm());
        return entity;
    }

    private Notificacao toDomain(NotificacaoEntity entity) {
        return new Notificacao(
                entity.getId(),
                entity.getConsultaId(),
                entity.getPacienteId(),
                entity.getDataHoraConsulta(),
                entity.getTipo(),
                entity.getStatus(),
                entity.getEnviadoEm(),
                entity.getCriadoEm()
        );
    }
}
