package com.techchallenge.appointment_service.application.usecase.consulta;

import com.techchallenge.appointment_service.application.dto.ConsultaRequestDTO;
import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.gateway.ConsultaGateway;
import com.techchallenge.appointment_service.domain.entity.Consulta;
import com.techchallenge.appointment_service.domain.entity.StatusConsulta;
import com.techchallenge.appointment_service.infrastructure.exception.BadRequestException;
import com.techchallenge.appointment_service.infrastructure.exception.EntityNotFoundException;
import com.techchallenge.appointment_service.infrastructure.messaging.NotificationEventDTO;
import com.techchallenge.appointment_service.infrastructure.messaging.RabbitMQConfig;
import com.techchallenge.appointment_service.infrastructure.security.AuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ConsultaUseCaseTest {

    private final ConsultaGateway gateway = mock(ConsultaGateway.class);
    private final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);

    @Test
    void deveCriarConsultaQuandoNaoHouverConflito() {
        CriarConsultaUseCase useCase = new CriarConsultaUseCase(gateway, rabbitTemplate);
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 15, 10, 0),
                StatusConsulta.AGENDADA,
                "Observação"
        );
        Consulta consultaSalva = new Consulta(
                UUID.randomUUID(),
                dto.pacienteId(),
                dto.medicoId(),
                dto.enfermeiroId(),
                dto.dataHora(),
                dto.status(),
                dto.observacoes(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(gateway.existeConsultaMedicoNoHorario(dto.medicoId(), dto.dataHora())).thenReturn(false);
        when(gateway.salvar(any(Consulta.class))).thenReturn(consultaSalva);

        ConsultaResponseDTO response = useCase.executar(dto);

        assertNotNull(response);
        assertEquals(consultaSalva.getId(), response.id());
        assertEquals(dto.pacienteId(), response.pacienteId());
        assertEquals(dto.medicoId(), response.medicoId());
        assertEquals(dto.status(), response.status());

        ArgumentCaptor<Consulta> captor = ArgumentCaptor.forClass(Consulta.class);
        verify(gateway).salvar(captor.capture());
        Consulta consultaEnviada = captor.getValue();
        assertEquals(dto.pacienteId(), consultaEnviada.getPacienteId());
        assertEquals(dto.medicoId(), consultaEnviada.getMedicoId());
        assertEquals(dto.dataHora(), consultaEnviada.getDataHora());

        ArgumentCaptor<NotificationEventDTO> eventCaptor = ArgumentCaptor.forClass(NotificationEventDTO.class);
        verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.QUEUE_NOTIFICATION), eventCaptor.capture());
        NotificationEventDTO evento = eventCaptor.getValue();
        assertEquals(response.id(), evento.consultaId());
        assertEquals(dto.pacienteId(), evento.pacienteId());
        assertEquals(dto.dataHora(), evento.dataHoraConsulta());
        assertEquals("LEMBRETE", evento.tipoNotificacao());
    }

    @Test
    void deveLancarExcecaoQuandoMedicoJaTemConsultaNoHorario() {
        CriarConsultaUseCase useCase = new CriarConsultaUseCase(gateway, rabbitTemplate);
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 15, 10, 0),
                StatusConsulta.AGENDADA,
                "Observação"
        );

        when(gateway.existeConsultaMedicoNoHorario(dto.medicoId(), dto.dataHora())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> useCase.executar(dto));
        verify(gateway, never()).salvar(any(Consulta.class));
        verifyNoInteractions(rabbitTemplate);
    }

    @Test
    void deveEditarConsultaEEnviarEvento() {
        EditarConsultaUseCase useCase = new EditarConsultaUseCase(gateway, rabbitTemplate);
        UUID id = UUID.randomUUID();
        Consulta consultaExistente = new Consulta(
                id,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 10, 9, 0),
                StatusConsulta.AGENDADA,
                "Anterior",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1)
        );
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                consultaExistente.getPacienteId(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 20, 14, 30),
                StatusConsulta.CONFIRMADA,
                "Atualizada"
        );

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(consultaExistente));
        when(gateway.salvar(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConsultaResponseDTO response = useCase.executar(id, dto);

        assertEquals(dto.dataHora(), response.dataHora());
        assertEquals(dto.status(), response.status());
        assertEquals(dto.observacoes(), response.observacoes());
        assertEquals(dto.medicoId(), response.medicoId());
        assertEquals(dto.enfermeiroId(), response.enfermeiroId());
        assertEquals(consultaExistente.getPacienteId(), response.pacienteId());

        assertEquals(dto.dataHora(), consultaExistente.getDataHora());
        assertEquals(dto.status(), consultaExistente.getStatus());
        assertEquals(dto.observacoes(), consultaExistente.getObservacoes());
        assertEquals(dto.medicoId(), consultaExistente.getMedicoId());
        assertEquals(dto.enfermeiroId(), consultaExistente.getEnfermeiroId());

        ArgumentCaptor<NotificationEventDTO> eventCaptor = ArgumentCaptor.forClass(NotificationEventDTO.class);
        verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.QUEUE_NOTIFICATION), eventCaptor.capture());
        NotificationEventDTO evento = eventCaptor.getValue();
        assertEquals(response.id(), evento.consultaId());
        assertEquals(response.pacienteId(), evento.pacienteId());
        assertEquals(response.dataHora(), evento.dataHoraConsulta());
        assertEquals("ALTERACAO", evento.tipoNotificacao());
    }

    @Test
    void deveLancarExcecaoQuandoConsultaParaEdicaoNaoExistir() {
        EditarConsultaUseCase useCase = new EditarConsultaUseCase(gateway, rabbitTemplate);
        UUID id = UUID.randomUUID();
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 20, 14, 30),
                StatusConsulta.CONFIRMADA,
                "Atualizada"
        );

        when(gateway.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.executar(id, dto));
        verify(gateway, never()).salvar(any(Consulta.class));
        verifyNoInteractions(rabbitTemplate);
    }

    @Test
    void deveListarConsultas() {
        ListarConsultasUseCase useCase = new ListarConsultasUseCase(gateway);
        Consulta primeira = new Consulta(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 15, 10, 0),
                StatusConsulta.AGENDADA,
                "Primeira",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Consulta segunda = new Consulta(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 16, 11, 0),
                StatusConsulta.CONFIRMADA,
                "Segunda",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(gateway.buscarTodos()).thenReturn(List.of(primeira, segunda));

        List<ConsultaResponseDTO> response = useCase.executar();

        assertEquals(2, response.size());
        assertEquals(primeira.getId(), response.get(0).id());
        assertEquals(segunda.getId(), response.get(1).id());
        assertEquals(primeira.getStatus(), response.get(0).status());
        assertEquals(segunda.getStatus(), response.get(1).status());
    }

    @Test
    void deveBuscarConsultasFuturasDoPaciente() {
        BuscarConsultasFuturasPacienteUseCase useCase = new BuscarConsultasFuturasPacienteUseCase(gateway);
        UUID pacienteId = UUID.randomUUID();
        Consulta consulta = new Consulta(
                UUID.randomUUID(),
                pacienteId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 20, 10, 0),
                StatusConsulta.AGENDADA,
                "Futura",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(gateway.buscarConsultasFuturasPorPacienteId(eq(pacienteId), any(LocalDateTime.class))).thenReturn(List.of(consulta));

        AuthenticatedUser usuario = new AuthenticatedUser(pacienteId, "paciente@email.com", "PACIENTE");
        List<ConsultaResponseDTO> response = useCase.executar(pacienteId, usuario);

        assertEquals(1, response.size());
        assertEquals(consulta.getId(), response.get(0).id());
        assertEquals(pacienteId, response.get(0).pacienteId());
        verify(gateway).buscarConsultasFuturasPorPacienteId(eq(pacienteId), any(LocalDateTime.class));
    }

    @Test
    void deveBuscarHistoricoDoPaciente() {
        BuscarHistoricoPacienteUseCase useCase = new BuscarHistoricoPacienteUseCase(gateway);
        UUID pacienteId = UUID.randomUUID();
        Consulta consulta = new Consulta(
                UUID.randomUUID(),
                pacienteId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 10, 10, 0),
                StatusConsulta.REALIZADA,
                "Histórico",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(gateway.buscarPorPacienteId(pacienteId)).thenReturn(List.of(consulta));

        AuthenticatedUser usuario = new AuthenticatedUser(pacienteId, "paciente@email.com", "PACIENTE");
        List<ConsultaResponseDTO> response = useCase.executar(pacienteId, usuario);

        assertEquals(1, response.size());
        assertEquals(consulta.getId(), response.get(0).id());
        assertEquals(pacienteId, response.get(0).pacienteId());
        verify(gateway).buscarPorPacienteId(pacienteId);
    }


    @Test
    void deveNegarHistoricoQuandoPacienteTentaAcessarOutroPaciente() {
        BuscarHistoricoPacienteUseCase useCase = new BuscarHistoricoPacienteUseCase(gateway);
        UUID pacienteConsultado = UUID.randomUUID();
        AuthenticatedUser usuario = new AuthenticatedUser(UUID.randomUUID(), "paciente@email.com", "PACIENTE");

        assertThrows(AccessDeniedException.class, () -> useCase.executar(pacienteConsultado, usuario));
        verify(gateway, never()).buscarPorPacienteId(pacienteConsultado);
    }

    @Test
    void deveNegarConsultasFuturasQuandoPacienteTentaAcessarOutroPaciente() {
        BuscarConsultasFuturasPacienteUseCase useCase = new BuscarConsultasFuturasPacienteUseCase(gateway);
        UUID pacienteConsultado = UUID.randomUUID();
        AuthenticatedUser usuario = new AuthenticatedUser(UUID.randomUUID(), "paciente@email.com", "PACIENTE");

        assertThrows(AccessDeniedException.class, () -> useCase.executar(pacienteConsultado, usuario));
        verify(gateway, never()).buscarConsultasFuturasPorPacienteId(eq(pacienteConsultado), any(LocalDateTime.class));
    }

    @Test
    void devePermitirMedicoConsultarHistoricoDeOutroPaciente() {
        BuscarHistoricoPacienteUseCase useCase = new BuscarHistoricoPacienteUseCase(gateway);
        UUID pacienteId = UUID.randomUUID();
        AuthenticatedUser medico = new AuthenticatedUser(UUID.randomUUID(), "medico@email.com", "MEDICO");

        when(gateway.buscarPorPacienteId(pacienteId)).thenReturn(List.of());

        useCase.executar(pacienteId, medico);

        verify(gateway).buscarPorPacienteId(pacienteId);
    }

    @Test
    void deveLancarExcecaoAoEditarParaHorarioJaOcupadoPeloMedico() {
        EditarConsultaUseCase useCase = new EditarConsultaUseCase(gateway, rabbitTemplate);
        UUID id = UUID.randomUUID();
        UUID medicoId = UUID.randomUUID();
        LocalDateTime novoHorario = LocalDateTime.of(2026, 8, 20, 14, 30);
        Consulta consultaExistente = new Consulta(
                id,
                UUID.randomUUID(),
                medicoId,
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 10, 9, 0),
                StatusConsulta.AGENDADA,
                "Anterior",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1)
        );
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                consultaExistente.getPacienteId(),
                medicoId,
                UUID.randomUUID(),
                novoHorario,
                StatusConsulta.CONFIRMADA,
                "Atualizada"
        );

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(consultaExistente));
        when(gateway.existeConsultaMedicoNoHorarioExcluindoId(medicoId, novoHorario, id)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> useCase.executar(id, dto));
        verify(gateway, never()).salvar(any(Consulta.class));
        verifyNoInteractions(rabbitTemplate);
    }

}