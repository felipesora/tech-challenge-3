package com.techchallenge.notification_service.infrastructure.api.controller;

import com.techchallenge.notification_service.application.dto.NotificacaoResponseDTO;
import com.techchallenge.notification_service.application.usecase.notificacao.ListarNotificacoesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@Tag(name = "Notificações", description = "Endpoints para o gerenciamento de notificações")
public class NotificacaoController {

    private final ListarNotificacoesUseCase listarNotificacoesUseCase;

    public NotificacaoController(ListarNotificacoesUseCase listarNotificacoesUseCase) {
        this.listarNotificacoesUseCase = listarNotificacoesUseCase;
    }

    @GetMapping
//    @PreAuthorize("hasRole('ENFERMEIRO')")
    @Operation(summary = "Listar todas as notificações", description = "Retorna uma lista com todas as notificações.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(listarNotificacoesUseCase.executar());
    }
}
