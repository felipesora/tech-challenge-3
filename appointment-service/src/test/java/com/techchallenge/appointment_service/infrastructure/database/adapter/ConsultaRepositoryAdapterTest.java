package com.techchallenge.appointment_service.infrastructure.database.adapter;

import com.techchallenge.appointment_service.domain.entity.Consulta;
import com.techchallenge.appointment_service.domain.entity.StatusConsulta;
import com.techchallenge.appointment_service.infrastructure.database.entity.ConsultaEntity;
import com.techchallenge.appointment_service.infrastructure.database.repository.ConsultaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConsultaRepositoryAdapterTest {

    private final ConsultaRepository repository = mock(ConsultaRepository.class);
    private final ConsultaRepositoryAdapter adapter = new ConsultaRepositoryAdapter(repository);

    @Test
    void deveSalvarConsultaConvertendoParaEntidade() {
        Consulta domain = new Consulta(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 20, 10, 0),
                StatusConsulta.AGENDADA,
                "Observação",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(repository.save(any(ConsultaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Consulta resultado = adapter.salvar(domain);

        ArgumentCaptor<ConsultaEntity> captor = ArgumentCaptor.forClass(ConsultaEntity.class);
        verify(repository).save(captor.capture());
        ConsultaEntity entity = captor.getValue();

        assertEquals(domain.getId(), resultado.getId());
        assertEquals(domain.getPacienteId(), resultado.getPacienteId());
        assertEquals(domain.getMedicoId(), resultado.getMedicoId());
        assertEquals(domain.getEnfermeiroId(), resultado.getEnfermeiroId());
        assertEquals(domain.getDataHora(), resultado.getDataHora());
        assertEquals(domain.getStatus(), resultado.getStatus());
        assertEquals(domain.getObservacoes(), resultado.getObservacoes());
        assertEquals(domain.getCriadoEm(), entity.getCriadoEm());
        assertEquals(domain.getAtualizadoEm(), entity.getAtualizadoEm());
    }

    @Test
    void deveBuscarTodasConsultasConvertendoParaDominio() {
        ConsultaEntity primeira = new ConsultaEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 20, 10, 0),
                StatusConsulta.AGENDADA,
                "Primeira",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        ConsultaEntity segunda = new ConsultaEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 21, 11, 0),
                StatusConsulta.CONFIRMADA,
                "Segunda",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(repository.findAll()).thenReturn(List.of(primeira, segunda));

        List<Consulta> resultado = adapter.buscarTodos();

        assertEquals(2, resultado.size());
        assertEquals(primeira.getId(), resultado.get(0).getId());
        assertEquals(segunda.getId(), resultado.get(1).getId());
        assertEquals(primeira.getStatus(), resultado.get(0).getStatus());
    }

    @Test
    void deveBuscarConsultasFuturasPorPaciente() {
        UUID pacienteId = UUID.randomUUID();
        LocalDateTime referencia = LocalDateTime.of(2026, 8, 20, 10, 0);
        ConsultaEntity entity = new ConsultaEntity(
                UUID.randomUUID(),
                pacienteId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                referencia.plusHours(1),
                StatusConsulta.AGENDADA,
                "Futura",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(repository.findByPacienteIdAndDataHoraAfter(pacienteId, referencia)).thenReturn(List.of(entity));

        List<Consulta> resultado = adapter.buscarConsultasFuturasPorPacienteId(pacienteId, referencia);

        assertEquals(1, resultado.size());
        assertEquals(entity.getId(), resultado.get(0).getId());
        assertEquals(pacienteId, resultado.get(0).getPacienteId());
    }

    @Test
    void deveBuscarPorIdEVerificarExistencia() {
        UUID id = UUID.randomUUID();
        ConsultaEntity entity = new ConsultaEntity(
                id,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 22, 9, 0),
                StatusConsulta.REALIZADA,
                "Existente",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.existsByMedicoIdAndDataHora(entity.getMedicoId(), entity.getDataHora())).thenReturn(true);

        Optional<Consulta> resultado = adapter.buscarPorId(id);
        boolean existe = adapter.existeConsultaMedicoNoHorario(entity.getMedicoId(), entity.getDataHora());

        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        assertTrue(existe);
    }


    @Test
    void deveVerificarConflitoDeHorarioExcluindoAConsultaAtual() {
        UUID consultaId = UUID.randomUUID();
        UUID medicoId = UUID.randomUUID();
        LocalDateTime dataHora = LocalDateTime.of(2026, 8, 22, 10, 0);

        when(repository.existsByMedicoIdAndDataHoraAndIdNot(medicoId, dataHora, consultaId)).thenReturn(true);

        boolean existe = adapter.existeConsultaMedicoNoHorarioExcluindoId(medicoId, dataHora, consultaId);

        assertTrue(existe);
        verify(repository).existsByMedicoIdAndDataHoraAndIdNot(medicoId, dataHora, consultaId);
    }

}