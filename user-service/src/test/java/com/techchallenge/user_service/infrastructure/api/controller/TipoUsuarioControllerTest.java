package com.techchallenge.user_service.infrastructure.api.controller;

import com.techchallenge.user_service.application.dto.TipoUsuarioRequestDTO;
import com.techchallenge.user_service.application.dto.TipoUsuarioResponseDTO;
import com.techchallenge.user_service.application.usecase.tipoUsuario.BuscarTipoUsuarioPorIdUseCase;
import com.techchallenge.user_service.application.usecase.tipoUsuario.CriarTipoUsuarioUseCase;
import com.techchallenge.user_service.application.usecase.tipoUsuario.ListarTiposUsuarioUseCase;
import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TipoUsuarioControllerTest {

    private MockMvc mockMvc;
    private final JsonMapper objectMapper = new JsonMapper();
    private final CriarTipoUsuarioUseCase criarUseCase = mock(CriarTipoUsuarioUseCase.class);
    private final ListarTiposUsuarioUseCase listarUseCase = mock(ListarTiposUsuarioUseCase.class);
    private final BuscarTipoUsuarioPorIdUseCase buscarUseCase = mock(BuscarTipoUsuarioPorIdUseCase.class);

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TipoUsuarioController(criarUseCase, listarUseCase, buscarUseCase))
                .setMessageConverters(new JacksonJsonHttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void deveCriarTipoUsuario() throws Exception {
        TipoUsuarioRequestDTO request = new TipoUsuarioRequestDTO(TipoUsuarioEnum.MEDICO);
        TipoUsuarioResponseDTO response = new TipoUsuarioResponseDTO(UUID.randomUUID(), TipoUsuarioEnum.MEDICO, true);
        when(criarUseCase.executar(any(TipoUsuarioRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/tipos-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/tipos-usuario/" + response.id())))
                .andExpect(jsonPath("$.id_tipo_usuario").value(response.id().toString()));
    }

    @Test
    void deveListarTiposUsuario() throws Exception {
        TipoUsuarioResponseDTO response = new TipoUsuarioResponseDTO(UUID.randomUUID(), TipoUsuarioEnum.PACIENTE, true);
        when(listarUseCase.executar()).thenReturn(List.of(response));

        mockMvc.perform(get("/tipos-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_tipo_usuario").value(response.id().toString()));
    }

    @Test
    void deveBuscarTipoUsuarioPorId() throws Exception {
        UUID id = UUID.randomUUID();
        TipoUsuarioResponseDTO response = new TipoUsuarioResponseDTO(id, TipoUsuarioEnum.ENFERMEIRO, true);
        when(buscarUseCase.executar(id)).thenReturn(response);

        mockMvc.perform(get("/tipos-usuario/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_tipo_usuario").value(id.toString()));
    }
}
