package com.techchallenge.user_service.infrastructure.api.controller;

import com.techchallenge.user_service.application.dto.UsuarioResponseDTO;
import com.techchallenge.user_service.application.usecase.usuario.BuscarUsuarioPorIdUseCase;
import com.techchallenge.user_service.application.usecase.usuario.ListarUsuariosUseCase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioControllerTest {

    private final ListarUsuariosUseCase listarUseCase = mock(ListarUsuariosUseCase.class);
    private final BuscarUsuarioPorIdUseCase buscarUseCase = mock(BuscarUsuarioPorIdUseCase.class);
    private final UsuarioController controller = new UsuarioController(listarUseCase, buscarUseCase);

    @Test
    void deveListarUsuarios() {
        UsuarioResponseDTO response = new UsuarioResponseDTO(UUID.randomUUID(), "Maria", "maria@email.com", "123.456.789-00", UUID.randomUUID(), true);
        when(listarUseCase.executar()).thenReturn(List.of(response));

        var resultado = controller.listarTodos();

        assertEquals(1, resultado.getBody().size());
        verify(listarUseCase).executar();
    }

    @Test
    void deveBuscarUsuarioPorId() {
        UUID id = UUID.randomUUID();
        UsuarioResponseDTO response = new UsuarioResponseDTO(id, "Maria", "maria@email.com", "123.456.789-00", UUID.randomUUID(), true);
        when(buscarUseCase.executar(id)).thenReturn(response);

        var resultado = controller.buscarPorId(id);

        assertEquals(id, resultado.getBody().id());
        verify(buscarUseCase).executar(id);
    }
}
