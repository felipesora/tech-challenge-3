package com.techchallenge.notification_service.infrastructure.config;

import com.techchallenge.notification_service.application.gateway.NotificacaoGateway;
import com.techchallenge.notification_service.application.gateway.NotificacaoSender;
import com.techchallenge.notification_service.application.usecase.notificacao.BuscarNotificacoesPorPacienteId;
import com.techchallenge.notification_service.application.usecase.notificacao.CriarNotificacaoUseCase;
import com.techchallenge.notification_service.application.usecase.notificacao.ListarNotificacoesUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ListarNotificacoesUseCase listarNotificacoesUseCase(NotificacaoGateway gateway) {
        return new ListarNotificacoesUseCase(gateway);
    }

    @Bean
    public BuscarNotificacoesPorPacienteId buscarNotificacoesPorPacienteId(NotificacaoGateway gateway) {
        return new BuscarNotificacoesPorPacienteId(gateway);
    }

    @Bean
    public CriarNotificacaoUseCase criarNotificacaoUseCase(NotificacaoGateway gateway, NotificacaoSender sender) {
        return new CriarNotificacaoUseCase(gateway, sender);
    }
}