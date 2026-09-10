package com.techchallenge.appointment_service.infrastructure.api.controller;

import com.techchallenge.appointment_service.application.dto.ConsultaRequestDTO;
import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.usecase.consulta.CriarConsultaUseCase;
import com.techchallenge.appointment_service.application.usecase.consulta.EditarConsultaUseCase;
import com.techchallenge.appointment_service.application.usecase.consulta.ListarConsultasUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/consultas")
@Tag(name = "Consultas", description = "Endpoints para o gerenciamento de consultas")
public class ConsultaController {

    private final CriarConsultaUseCase criarConsultaUseCase;
    private final ListarConsultasUseCase listarConsultasUseCase;
    private final EditarConsultaUseCase editarConsultaUseCase;

    public ConsultaController(CriarConsultaUseCase criarConsultaUseCase, ListarConsultasUseCase listarConsultasUseCase, EditarConsultaUseCase editarConsultaUseCase) {
        this.criarConsultaUseCase = criarConsultaUseCase;
        this.listarConsultasUseCase = listarConsultasUseCase;
        this.editarConsultaUseCase = editarConsultaUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO')")
    @Operation(summary = "Cadastrar uma nova consulta", description = "Cria uma nova consulta, com paciente, médico e enfermeiro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consulta cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<ConsultaResponseDTO> criar(@RequestBody @Valid ConsultaRequestDTO request, UriComponentsBuilder uriBuilder) {
        ConsultaResponseDTO response = criarConsultaUseCase.executar(request);
        URI endereco = uriBuilder.path("/consultas/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ENFERMEIRO')")
    @Operation(summary = "Listar todas as consultas", description = "Retorna uma lista com todas as consultas.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    public ResponseEntity<List<ConsultaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(listarConsultasUseCase.executar());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDICO'')")
    @Operation(summary = "Editar uma consulta")
    public ResponseEntity<ConsultaResponseDTO> editar(@PathVariable UUID id, @RequestBody @Valid ConsultaRequestDTO request) {
        return ResponseEntity.ok(editarConsultaUseCase.executar(id, request));
    }
}