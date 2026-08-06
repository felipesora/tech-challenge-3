package com.techchallenge.notification_service.infrastructure.api.controller;

import com.techchallenge.notification_service.application.dto.NotificacaoResponseDTO;
import com.techchallenge.notification_service.application.usecase.notificacao.BuscarNotificacoesPorPacienteId;
import com.techchallenge.notification_service.application.usecase.notificacao.ListarNotificacoesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notificacoes")
@Tag(name = "Notificações", description = "Endpoints para o gerenciamento de notificações")
public class NotificacaoController {

    private final ListarNotificacoesUseCase listarNotificacoesUseCase;
    private final BuscarNotificacoesPorPacienteId buscarNotificacoesPorPacienteId;

    public NotificacaoController(ListarNotificacoesUseCase listarNotificacoesUseCase,
                                 BuscarNotificacoesPorPacienteId buscarNotificacoesPorPacienteId) {
        this.listarNotificacoesUseCase = listarNotificacoesUseCase;
        this.buscarNotificacoesPorPacienteId = buscarNotificacoesPorPacienteId;
    }

    @GetMapping
//    @PreAuthorize("hasRole('ENFERMEIRO')")
    @Operation(summary = "Listar todas as notificações", description = "Retorna uma lista com todas as notificações.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    public ResponseEntity<List<NotificacaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(listarNotificacoesUseCase.executar());
    }

    @GetMapping("/paciente/{id}")
    @Operation(summary = "Buscar notificações por paciente", description = "Retorna todas as notificações de um paciente.")
    @ApiResponse(responseCode = "200", description = "Notificações recuperadas com sucesso")
    public ResponseEntity<List<NotificacaoResponseDTO>> buscarPorPacienteId(@PathVariable UUID id) {
        return ResponseEntity.ok(buscarNotificacoesPorPacienteId.executar(id));
    }
}
