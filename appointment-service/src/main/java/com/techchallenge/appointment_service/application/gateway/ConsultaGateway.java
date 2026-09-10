package com.techchallenge.appointment_service.application.gateway;

import com.techchallenge.appointment_service.domain.entity.Consulta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultaGateway {
    Consulta salvar(Consulta consulta);

    List<Consulta> buscarTodos();

    boolean existeConsultaMedicoNoHorario(UUID medicoId, LocalDateTime dataHora);

    boolean existeConsultaMedicoNoHorarioExcluindoId(UUID medicoId, LocalDateTime dataHora, UUID consultaId);

    List<Consulta> buscarPorPacienteId(UUID pacienteId);

    List<Consulta> buscarConsultasFuturasPorPacienteId(UUID pacienteId, LocalDateTime dataHora);

    Optional<Consulta> buscarPorId(UUID id);
}
