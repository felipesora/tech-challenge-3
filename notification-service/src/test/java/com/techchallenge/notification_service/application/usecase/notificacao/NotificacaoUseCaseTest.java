package com.techchallenge.notification_service.application.usecase.notificacao;

import com.techchallenge.notification_service.application.dto.NotificacaoResponseDTO;
import com.techchallenge.notification_service.application.gateway.NotificacaoGateway;
import com.techchallenge.notification_service.application.gateway.NotificacaoSender;
import com.techchallenge.notification_service.domain.entity.Notificacao;
import com.techchallenge.notification_service.domain.entity.StatusNotificacao;
import com.techchallenge.notification_service.domain.entity.TipoNotificacao;
import com.techchallenge.notification_service.infrastructure.messaging.NotificationEventDTO;
import com.techchallenge.notification_service.infrastructure.security.AuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificacaoUseCaseTest {

    private final NotificacaoGateway gateway = mock(NotificacaoGateway.class);
    private final NotificacaoSender sender = mock(NotificacaoSender.class);

    @Test
    void deveCriarEnviarEAtualizarNotificacao() {
        CriarNotificacaoUseCase useCase = new CriarNotificacaoUseCase(gateway, sender);
        UUID consultaId = UUID.randomUUID();
        UUID pacienteId = UUID.randomUUID();
        LocalDateTime dataHora = LocalDateTime.of(2026, 8, 20, 10, 0);
        NotificationEventDTO evento = new NotificationEventDTO(consultaId, pacienteId, dataHora, "LEMBRETE");

        when(gateway.salvar(any(Notificacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        useCase.executar(evento);

        ArgumentCaptor<Notificacao> captor = ArgumentCaptor.forClass(Notificacao.class);
        verify(gateway, times(2)).salvar(captor.capture());
        verify(sender).enviar(any(Notificacao.class));

        Notificacao notificacaoFinal = captor.getAllValues().get(1);
        assertEquals(StatusNotificacao.ENVIADA, notificacaoFinal.getStatus());
        assertNotNull(notificacaoFinal.getEnviadoEm());
        assertEquals(consultaId, notificacaoFinal.getConsultaId());
        assertEquals(pacienteId, notificacaoFinal.getPacienteId());
    }

    @Test
    void deveMarcarNotificacaoComoErroQuandoEnvioFalhar() {
        CriarNotificacaoUseCase useCase = new CriarNotificacaoUseCase(gateway, sender);
        NotificationEventDTO evento = new NotificationEventDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 20, 10, 0),
                "LEMBRETE"
        );

        when(gateway.salvar(any(Notificacao.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doThrow(new RuntimeException("Falha no envio")).when(sender).enviar(any(Notificacao.class));

        useCase.executar(evento);

        ArgumentCaptor<Notificacao> captor = ArgumentCaptor.forClass(Notificacao.class);
        verify(gateway, times(2)).salvar(captor.capture());
        Notificacao notificacaoFinal = captor.getAllValues().get(1);

        assertEquals(StatusNotificacao.ERRO, notificacaoFinal.getStatus());
        assertNull(notificacaoFinal.getEnviadoEm());
    }

    @Test
    void deveListarNotificacoes() {
        ListarNotificacoesUseCase useCase = new ListarNotificacoesUseCase(gateway);
        Notificacao notificacao = new Notificacao(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.now(),
                TipoNotificacao.LEMBRETE,
                StatusNotificacao.PENDENTE,
                null,
                LocalDateTime.now()
        );
        when(gateway.buscarTodos()).thenReturn(List.of(notificacao));

        List<NotificacaoResponseDTO> response = useCase.executar();

        assertEquals(1, response.size());
        assertEquals(notificacao.getId(), response.get(0).id());
        assertEquals(TipoNotificacao.LEMBRETE, response.get(0).tipo());
    }

    @Test
    void deveBuscarNotificacoesPorPacienteQuandoUsuarioEhPacienteProprio() {
        BuscarNotificacoesPorPacienteId useCase = new BuscarNotificacoesPorPacienteId(gateway);
        UUID pacienteId = UUID.randomUUID();
        AuthenticatedUser usuario = new AuthenticatedUser(pacienteId, "paciente@email.com", "PACIENTE");
        Notificacao notificacao = new Notificacao(
                UUID.randomUUID(),
                UUID.randomUUID(),
                pacienteId,
                LocalDateTime.now(),
                TipoNotificacao.ALTERACAO,
                StatusNotificacao.ENVIADA,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(gateway.buscarNotificacoesPorPacienteId(pacienteId)).thenReturn(List.of(notificacao));

        List<NotificacaoResponseDTO> response = useCase.executar(pacienteId, usuario);

        assertEquals(1, response.size());
        assertEquals(pacienteId, response.get(0).pacienteId());
    }

    @Test
    void deveNegarBuscaDeNotificacoesQuandoPacienteTentaAcessarOutroPaciente() {
        BuscarNotificacoesPorPacienteId useCase = new BuscarNotificacoesPorPacienteId(gateway);
        UUID pacienteId = UUID.randomUUID();
        AuthenticatedUser usuario = new AuthenticatedUser(UUID.randomUUID(), "paciente@email.com", "PACIENTE");

        assertThrows(AccessDeniedException.class, () -> useCase.executar(pacienteId, usuario));
    }
}