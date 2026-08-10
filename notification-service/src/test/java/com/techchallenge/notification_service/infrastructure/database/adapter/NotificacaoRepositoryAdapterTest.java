package com.techchallenge.notification_service.infrastructure.database.adapter;

import com.techchallenge.notification_service.domain.entity.Notificacao;
import com.techchallenge.notification_service.domain.entity.StatusNotificacao;
import com.techchallenge.notification_service.domain.entity.TipoNotificacao;
import com.techchallenge.notification_service.infrastructure.database.entity.NotificacaoEntity;
import com.techchallenge.notification_service.infrastructure.database.repository.NotificacaoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificacaoRepositoryAdapterTest {

    private final NotificacaoRepository repository = mock(NotificacaoRepository.class);
    private final NotificacaoRepositoryAdapter adapter = new NotificacaoRepositoryAdapter(repository);

    @Test
    void deveSalvarNotificacaoConvertendoParaEntidade() {
        Notificacao domain = new Notificacao(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.of(2026, 8, 20, 10, 0), TipoNotificacao.LEMBRETE, StatusNotificacao.PENDENTE, LocalDateTime.now(), LocalDateTime.now());

        when(repository.save(any(NotificacaoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Notificacao resultado = adapter.salvar(domain);

        ArgumentCaptor<NotificacaoEntity> captor = ArgumentCaptor.forClass(NotificacaoEntity.class);
        verify(repository).save(captor.capture());

        assertEquals(domain.getId(), resultado.getId());
        assertEquals(domain.getConsultaId(), resultado.getConsultaId());
        assertEquals(domain.getPacienteId(), resultado.getPacienteId());
        assertEquals(domain.getTipo(), resultado.getTipo());
        assertEquals(domain.getStatus(), resultado.getStatus());
    }

    @Test
    void deveBuscarTodasNotificacoes() {
        NotificacaoEntity entity = new NotificacaoEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), TipoNotificacao.ALTERACAO, StatusNotificacao.PENDENTE, LocalDateTime.now(), LocalDateTime.now());
        when(repository.findAll()).thenReturn(List.of(entity));

        List<Notificacao> resultado = adapter.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals(entity.getId(), resultado.get(0).getId());
        assertEquals(entity.getTipo(), resultado.get(0).getTipo());
    }

    @Test
    void deveBuscarNotificacoesPorPaciente() {
        UUID pacienteId = UUID.randomUUID();
        NotificacaoEntity entity = new NotificacaoEntity(UUID.randomUUID(), UUID.randomUUID(), pacienteId, LocalDateTime.now(), TipoNotificacao.LEMBRETE, StatusNotificacao.PENDENTE, LocalDateTime.now(), LocalDateTime.now());
        when(repository.findByPacienteId(pacienteId)).thenReturn(List.of(entity));

        List<Notificacao> resultado = adapter.buscarNotificacoesPorPacienteId(pacienteId);

        assertEquals(1, resultado.size());
        assertEquals(pacienteId, resultado.get(0).getPacienteId());
    }
}
