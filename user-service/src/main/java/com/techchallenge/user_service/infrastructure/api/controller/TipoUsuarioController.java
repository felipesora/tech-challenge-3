package com.techchallenge.user_service.infrastructure.api.controller;

import com.techchallenge.user_service.application.dto.TipoUsuarioRequestDTO;
import com.techchallenge.user_service.application.dto.TipoUsuarioResponseDTO;
import com.techchallenge.user_service.application.usecase.usuario.BuscarTipoUsuarioPorIdUseCase;
import com.techchallenge.user_service.application.usecase.usuario.CriarTipoUsuarioUseCase;
import com.techchallenge.user_service.application.usecase.usuario.ListarTiposUsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tipos-usuario")
@Tag(name = "Tipos de Usuário", description = "Endpoints para o gerenciamento de categorias de usuários (ex: MEDICO, ENFERMEIRO, PACIENTE)")
public class TipoUsuarioController {

    private final CriarTipoUsuarioUseCase criarTipoUsuarioUseCase;
    private final ListarTiposUsuarioUseCase listarTiposUsuarioUseCase;
    private final BuscarTipoUsuarioPorIdUseCase buscarTipoUsuarioPorIdUseCase;

    public TipoUsuarioController(CriarTipoUsuarioUseCase criarTipoUsuarioUseCase,
                                 ListarTiposUsuarioUseCase listarTiposUsuarioUseCase,
                                 BuscarTipoUsuarioPorIdUseCase buscarTipoUsuarioPorIdUseCase) {
        this.criarTipoUsuarioUseCase = criarTipoUsuarioUseCase;
        this.listarTiposUsuarioUseCase = listarTiposUsuarioUseCase;
        this.buscarTipoUsuarioPorIdUseCase = buscarTipoUsuarioPorIdUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo tipo de usuário", description = "Cria uma nova categoria de perfil no sistema (ex: MEDICO, ENFERMEIRO, PACIENTE).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<TipoUsuarioResponseDTO> criar(@RequestBody @Valid TipoUsuarioRequestDTO dto, UriComponentsBuilder uriBuilder) {
        TipoUsuarioResponseDTO response = criarTipoUsuarioUseCase.executar(dto);
        URI endereco = uriBuilder.path("/tipos-usuario/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os tipos de usuários", description = "Retorna uma lista com todas os tipos cadastrados, ativos e inativos.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    public ResponseEntity<List<TipoUsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(listarTiposUsuarioUseCase.executar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de usuário por ID", description = "Busca os detalhes de um tipo específico utilizando o seu UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado")
    })
    public ResponseEntity<TipoUsuarioResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(buscarTipoUsuarioPorIdUseCase.executar(id));
    }
}
