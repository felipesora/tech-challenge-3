package com.techchallenge.user_service.infrastructure.api.controller;

import com.techchallenge.user_service.application.dto.UsuarioResponseDTO;
import com.techchallenge.user_service.application.usecase.usuario.BuscarUsuarioPorIdUseCase;
import com.techchallenge.user_service.application.usecase.usuario.ListarUsuariosUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoints para o gerenciamento de usuários (MEDICO, ENFERMEIRO, PACIENTE)")
public class UsuarioController {

    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    public UsuarioController(ListarUsuariosUseCase listarUsuariosUseCase,
                             BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase) {
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MEDICO','ENFERMEIRO')")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna uma lista contendo todos os usuários cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(listarUsuariosUseCase.executar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("""
    hasAnyRole('MEDICO','ENFERMEIRO')
    or
    #id == authentication.principal.id
    """)
    @Operation(summary = "Buscar tipo de usuário por ID", description = "Busca os detalhes de um usuário específico utilizando o seu UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(buscarUsuarioPorIdUseCase.executar(id));
    }
}
