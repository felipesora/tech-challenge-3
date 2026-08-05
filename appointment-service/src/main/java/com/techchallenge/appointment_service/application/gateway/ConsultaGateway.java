package com.techchallenge.appointment_service.application.gateway;

import com.techchallenge.appointment_service.domain.entity.Consulta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ConsultaGateway {
    Consulta salvar(Consulta consulta);

    List<Consulta> buscarTodos();

    boolean existeConsultaMedicoNoHorario(UUID medicoId, LocalDateTime dataHora);
}
