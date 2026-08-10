package com.techchallenge.appointment_service.infrastructure.config;

import com.techchallenge.appointment_service.application.gateway.ConsultaGateway;
import com.techchallenge.appointment_service.application.usecase.consulta.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CriarConsultaUseCase criarConsultaUseCase(ConsultaGateway gateway, RabbitTemplate rabbitTemplate) {
        return new CriarConsultaUseCase(gateway, rabbitTemplate);
    }

    @Bean
    public ListarConsultasUseCase listarConsultasUseCase(ConsultaGateway gateway) {
        return new ListarConsultasUseCase(gateway);
    }

    @Bean
    public EditarConsultaUseCase editarConsultaUseCase(ConsultaGateway gateway, RabbitTemplate rabbitTemplate) {
        return new EditarConsultaUseCase(gateway, rabbitTemplate);
    }

    @Bean
    public BuscarHistoricoPacienteUseCase buscarHistoricoPacienteUseCase(ConsultaGateway gateway) {
        return new BuscarHistoricoPacienteUseCase(gateway);
    }

    @Bean
    public BuscarConsultasFuturasPacienteUseCase buscarConsultasFuturasPacienteUseCase(ConsultaGateway gateway) {
        return new BuscarConsultasFuturasPacienteUseCase(gateway);
    }
}