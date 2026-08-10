package com.techchallenge.appointment_service.infrastructure.api.controller;

import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.usecase.consulta.BuscarConsultasFuturasPacienteUseCase;
import com.techchallenge.appointment_service.application.usecase.consulta.BuscarHistoricoPacienteUseCase;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class ConsultaGraphQLController {

    private final BuscarHistoricoPacienteUseCase buscarHistoricoPacienteUseCase;
    private final BuscarConsultasFuturasPacienteUseCase buscarConsultasFuturasPacienteUseCase;

    public ConsultaGraphQLController(BuscarHistoricoPacienteUseCase buscarHistoricoPacienteUseCase, BuscarConsultasFuturasPacienteUseCase buscarConsultasFuturasPacienteUseCase) {
        this.buscarHistoricoPacienteUseCase = buscarHistoricoPacienteUseCase;
        this.buscarConsultasFuturasPacienteUseCase = buscarConsultasFuturasPacienteUseCase;
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO','PACIENTE')")
    public List<ConsultaResponseDTO> historicoPaciente(@Argument UUID pacienteId) {
        return buscarHistoricoPacienteUseCase.executar(pacienteId);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO','PACIENTE')")
    public List<ConsultaResponseDTO> consultasFuturasPaciente(@Argument UUID pacienteId) {
        return buscarConsultasFuturasPacienteUseCase.executar(pacienteId);
    }
}