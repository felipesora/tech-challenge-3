package com.techchallenge.appointment_service.application.usecase.consulta;

import com.techchallenge.appointment_service.application.dto.ConsultaRequestDTO;
import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.gateway.ConsultaGateway;
import com.techchallenge.appointment_service.domain.entity.Consulta;
import com.techchallenge.appointment_service.infrastructure.exception.BadRequestException;
import com.techchallenge.appointment_service.infrastructure.messaging.NotificationEventDTO;
import com.techchallenge.appointment_service.infrastructure.messaging.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

public class CriarConsultaUseCase {
    private final ConsultaGateway gateway;
    private final RabbitTemplate rabbitTemplate;

    public CriarConsultaUseCase(ConsultaGateway gateway, RabbitTemplate rabbitTemplate) {
        this.gateway = gateway;
        this.rabbitTemplate = rabbitTemplate;
    }

    public ConsultaResponseDTO executar(ConsultaRequestDTO dto) {
        if (gateway.existeConsultaMedicoNoHorario(dto.medicoId(), dto.dataHora())) {
            throw new BadRequestException("O médico já possui uma consulta agendada para esse horário.");
        }

        Consulta consulta = new Consulta(
                null,
                dto.pacienteId(),
                dto.medicoId(),
                dto.enfermeiroId(),
                dto.dataHora(),
                dto.status(),
                dto.observacoes(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        ConsultaResponseDTO responseDTO = ConsultaResponseDTO.fromDomain(gateway.salvar(consulta));

        var evento = new NotificationEventDTO(responseDTO.id(), dto.pacienteId(), dto.dataHora(), "LEMBRETE");

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.QUEUE_NOTIFICATION,
                evento
        );

        return responseDTO;
    }
}