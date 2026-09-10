package com.techchallenge.appointment_service.infrastructure.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveRetornarErroDeValidacao() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        bindingResult.addError(new FieldError("object", "pacienteId", "must not be null", false, null, null, "Id do paciente é obrigatório"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(exception);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Erro de validação", response.getBody().getMessage());
        assertTrue(response.getBody().getErrors().containsKey("pacienteId"));
    }

    @Test
    void deveRetornarErroDeEntidadeNaoEncontrada() {
        EntityNotFoundException exception = new EntityNotFoundException("Consulta não encontrada");

        ResponseEntity<ErrorResponse> response = handler.handleEntityNotFoundException(exception);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Consulta não encontrada", response.getBody().getMessage());
    }

    @Test
    void deveRetornarErroDeBadRequest() {
        BadRequestException exception = new BadRequestException("Horário indisponível");

        ResponseEntity<ErrorResponse> response = handler.handleBadRequestException(exception);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Horário indisponível", response.getBody().getMessage());
    }

    @Test
    void deveRetornarErroDePermissao() {
        AccessDeniedException exception = new AccessDeniedException("Acesso negado");

        ResponseEntity<ErrorResponse> response = handler.handleAccessDeniedException(exception);

        assertEquals(403, response.getStatusCode().value());
        assertEquals("Usuário sem permissão para acessar este recurso", response.getBody().getMessage());
    }

    @Test
    void deveRetornarErroDeCredenciaisInvalidas() {
        BadCredentialsException exception = new BadCredentialsException("Credenciais inválidas");

        ResponseEntity<ErrorResponse> response = handler.handleBadCredentials(exception);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("Credenciais inválidas", response.getBody().getMessage());
    }

    @Test
    void deveRetornarErroParaTipoDeParametroInvalido() {
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException("abc", UUID.class, "id", null, new IllegalArgumentException("invalid uuid"));

        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentTypeMismatch(exception);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody().getMessage().contains("UUID válido"));
    }

    @Test
    void deveRetornarErroParaCorpoInvalido() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Corpo inválido", null);

        ResponseEntity<ErrorResponse> response = handler.handleHttpMessageNotReadable(exception);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody().getMessage());
    }

}
