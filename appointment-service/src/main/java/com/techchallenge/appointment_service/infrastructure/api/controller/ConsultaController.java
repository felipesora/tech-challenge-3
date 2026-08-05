package com.techchallenge.appointment_service.infrastructure.api.controller;

import com.techchallenge.appointment_service.application.dto.ConsultaRequestDTO;
import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.usecase.consulta.CriarConsultaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/consultas")
@Tag(name = "Consultas", description = "Endpoints para o gerenciamento de consultas")
public class ConsultaController {

    private final CriarConsultaUseCase criarConsultaUseCase;

    public ConsultaController(CriarConsultaUseCase criarConsultaUseCase) {
        this.criarConsultaUseCase = criarConsultaUseCase;
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
}
