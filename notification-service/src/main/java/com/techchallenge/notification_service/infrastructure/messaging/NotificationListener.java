package com.techchallenge.notification_service.infrastructure.messaging;

import com.techchallenge.notification_service.application.usecase.notificacao.CriarNotificacaoUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final CriarNotificacaoUseCase criarNotificacaoUseCase;

    public NotificationListener(CriarNotificacaoUseCase criarNotificacaoUseCase) {
        this.criarNotificacaoUseCase = criarNotificacaoUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICATION)
    public void processarNotificacao(NotificationEventDTO evento) {
        System.out.println("Recebido evento de consulta: " + evento.consultaId());
        criarNotificacaoUseCase.executar(evento);
    }
}