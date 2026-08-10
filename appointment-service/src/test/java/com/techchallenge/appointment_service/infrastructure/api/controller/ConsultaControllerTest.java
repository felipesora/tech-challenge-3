package com.techchallenge.appointment_service.infrastructure.api.controller;

import com.techchallenge.appointment_service.application.dto.ConsultaRequestDTO;
import com.techchallenge.appointment_service.application.dto.ConsultaResponseDTO;
import com.techchallenge.appointment_service.application.usecase.consulta.CriarConsultaUseCase;
import com.techchallenge.appointment_service.application.usecase.consulta.EditarConsultaUseCase;
import com.techchallenge.appointment_service.application.usecase.consulta.ListarConsultasUseCase;
import com.techchallenge.appointment_service.domain.entity.StatusConsulta;
import com.techchallenge.appointment_service.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConsultaControllerTest {

    private MockMvc mockMvc;
    private final JsonMapper objectMapper = new JsonMapper();
    private final CriarConsultaUseCase criarConsultaUseCase = mock(CriarConsultaUseCase.class);
    private final ListarConsultasUseCase listarConsultasUseCase = mock(ListarConsultasUseCase.class);
    private final EditarConsultaUseCase editarConsultaUseCase = mock(EditarConsultaUseCase.class);

    @BeforeEach
    void setup() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new ConsultaController(criarConsultaUseCase, listarConsultasUseCase, editarConsultaUseCase))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .setMessageConverters(new JacksonJsonHttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void deveCriarConsulta() throws Exception {
        UUID id = UUID.randomUUID();
        ConsultaRequestDTO request = new ConsultaRequestDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 20, 10, 0),
                StatusConsulta.AGENDADA,
                "Observação"
        );
        ConsultaResponseDTO response = new ConsultaResponseDTO(
                id,
                request.pacienteId(),
                request.medicoId(),
                request.enfermeiroId(),
                request.dataHora(),
                request.status(),
                request.observacoes(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(criarConsultaUseCase.executar(any(ConsultaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/consultas/" + id)))
                .andExpect(jsonPath("$.id_consulta").value(id.toString()));
    }

    @Test
    void deveRetornarBadRequestQuandoDadosForemInvalidos() throws Exception {
        String payload = "{}";

        mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Erro de validação"));
    }

    @Test
    void deveListarConsultas() throws Exception {
        ConsultaResponseDTO response = new ConsultaResponseDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 21, 12, 0),
                StatusConsulta.CONFIRMADA,
                "Lista",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(listarConsultasUseCase.executar()).thenReturn(List.of(response));

        mockMvc.perform(get("/consultas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_consulta").value(response.id().toString()));
    }

    @Test
    void deveEditarConsulta() throws Exception {
        UUID id = UUID.randomUUID();
        ConsultaRequestDTO request = new ConsultaRequestDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.of(2026, 8, 22, 8, 30),
                StatusConsulta.REALIZADA,
                "Atualizada"
        );
        ConsultaResponseDTO response = new ConsultaResponseDTO(
                id,
                request.pacienteId(),
                request.medicoId(),
                request.enfermeiroId(),
                request.dataHora(),
                request.status(),
                request.observacoes(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(editarConsultaUseCase.executar(any(UUID.class), any(ConsultaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/consultas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_consulta").value(id.toString()));
    }
}
