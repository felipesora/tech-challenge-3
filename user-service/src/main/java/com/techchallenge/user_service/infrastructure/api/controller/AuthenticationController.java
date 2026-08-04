package com.techchallenge.user_service.infrastructure.api.controller;

import com.techchallenge.user_service.application.dto.LoginRequestDTO;
import com.techchallenge.user_service.application.dto.TokenResponseDTO;
import com.techchallenge.user_service.application.dto.UsuarioRequestDTO;
import com.techchallenge.user_service.application.dto.UsuarioResponseDTO;
import com.techchallenge.user_service.application.usecase.auth.CriarUsuarioUseCase;
import com.techchallenge.user_service.application.usecase.auth.RealizarLoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints responsáveis pelo cadastro e autenticação de usuários.")
public class AuthenticationController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final RealizarLoginUseCase realizarLoginUseCase;

    public AuthenticationController(CriarUsuarioUseCase criarUsuarioUseCase,
                                    RealizarLoginUseCase realizarLoginUseCase) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.realizarLoginUseCase = realizarLoginUseCase;
    }

    @PostMapping("/register")
    @Operation(summary = "Criar um novo usuário", description = "Cadastra um novo usuário no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody @Valid UsuarioRequestDTO dto, UriComponentsBuilder uriBuilder) {
        UsuarioResponseDTO response = criarUsuarioUseCase.executar(dto);
        URI endereco = uriBuilder.path("/usuarios/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica um usuário e retorna um token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public TokenResponseDTO login(@RequestBody @Valid LoginRequestDTO dto) {
        return realizarLoginUseCase.execute(dto);
    }
}
