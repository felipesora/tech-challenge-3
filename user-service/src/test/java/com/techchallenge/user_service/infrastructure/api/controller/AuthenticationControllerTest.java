package com.techchallenge.user_service.infrastructure.api.controller;

import com.techchallenge.user_service.application.dto.LoginRequestDTO;
import com.techchallenge.user_service.application.dto.TokenResponseDTO;
import com.techchallenge.user_service.application.dto.UsuarioRequestDTO;
import com.techchallenge.user_service.application.dto.UsuarioResponseDTO;
import com.techchallenge.user_service.application.usecase.auth.CriarUsuarioUseCase;
import com.techchallenge.user_service.application.usecase.auth.RealizarLoginUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest {

    private MockMvc mockMvc;
    private final JsonMapper objectMapper = new JsonMapper();
    private final CriarUsuarioUseCase criarUsuarioUseCase = mock(CriarUsuarioUseCase.class);
    private final RealizarLoginUseCase realizarLoginUseCase = mock(RealizarLoginUseCase.class);

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthenticationController(criarUsuarioUseCase, realizarLoginUseCase))
                .setMessageConverters(new JacksonJsonHttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void deveCriarUsuario() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO("Maria", "maria@email.com", "123.456.789-00", "123456", UUID.randomUUID());
        UsuarioResponseDTO response = new UsuarioResponseDTO(UUID.randomUUID(), "Maria", "maria@email.com", "123.456.789-00", request.tipoUsuarioId(), true);
        when(criarUsuarioUseCase.executar(any(UsuarioRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/usuarios/" + response.id())))
                .andExpect(jsonPath("$.id_usuario").value(response.id().toString()));
    }

    @Test
    void deveRealizarLogin() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("maria@email.com", "123456");
        TokenResponseDTO response = new TokenResponseDTO("token-123", 3600L);
        when(realizarLoginUseCase.execute(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-123"));
    }
}
