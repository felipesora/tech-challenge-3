package com.techchallenge.appointment_service.infrastructure.config;

import com.techchallenge.appointment_service.application.gateway.ConsultaGateway;
import com.techchallenge.appointment_service.application.usecase.consulta.CriarConsultaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CriarConsultaUseCase criarConsultaUseCase(ConsultaGateway gateway) {
        return new CriarConsultaUseCase(gateway);
    }
}
