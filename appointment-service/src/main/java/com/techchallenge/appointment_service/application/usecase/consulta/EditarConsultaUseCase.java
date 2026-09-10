package com.techchallenge.appointment_service.application.usecase.consulta;

import com.techchallenge.appointment_service.application.dto.ConsultaRequestDTO;
import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.gateway.ConsultaGateway;
import com.techchallenge.appointment_service.domain.entity.Consulta;
import com.techchallenge.appointment_service.infrastructure.exception.BadRequestException;
import com.techchallenge.appointment_service.infrastructure.exception.EntityNotFoundException;
import com.techchallenge.appointment_service.infrastructure.messaging.NotificationEventDTO;
import com.techchallenge.appointment_service.infrastructure.messaging.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

public class EditarConsultaUseCase {
    private final ConsultaGateway gateway;
    private final RabbitTemplate rabbitTemplate;

    public EditarConsultaUseCase(ConsultaGateway gateway, RabbitTemplate rabbitTemplate) {
        this.gateway = gateway;
        this.rabbitTemplate = rabbitTemplate;
    }

    public ConsultaResponseDTO executar(UUID id, ConsultaRequestDTO dto) {
        Consulta consulta = gateway.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Nenhuma consulta foi encontrada com o ID informado: " + id));

        if (gateway.existeConsultaMedicoNoHorarioExcluindoId(dto.medicoId(), dto.dataHora(), id)) {
            throw new BadRequestException("O médico já possui outra consulta agendada para esse horário.");
        }

        consulta.setDataHora(dto.dataHora());
        consulta.setStatus(dto.status());
        consulta.setObservacoes(dto.observacoes());
        consulta.setMedicoId(dto.medicoId());
        consulta.setEnfermeiroId(dto.enfermeiroId());
        consulta.setAtualizadoEm(LocalDateTime.now());

        ConsultaResponseDTO response = ConsultaResponseDTO.fromDomain(gateway.salvar(consulta));

        var evento = new NotificationEventDTO(
                response.id(),
                response.pacienteId(),
                response.dataHora(),
                "ALTERACAO"
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.QUEUE_NOTIFICATION,
                evento
        );

        return response;
    }
}