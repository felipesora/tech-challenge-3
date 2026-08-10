package com.techchallenge.user_service.application.usecase.usuario;

import com.techchallenge.user_service.application.dto.UsuarioResponseDTO;
import com.techchallenge.user_service.application.gateway.UsuarioGateway;
import com.techchallenge.user_service.domain.entity.TipoUsuario;
import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import com.techchallenge.user_service.domain.entity.Usuario;
import com.techchallenge.user_service.infrastructure.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioUseCaseTest {

    private final UsuarioGateway gateway = mock(UsuarioGateway.class);

    @Test
    void deveBuscarUsuarioPorId() {
        BuscarUsuarioPorIdUseCase useCase = new BuscarUsuarioPorIdUseCase(gateway);
        UUID id = UUID.randomUUID();
        TipoUsuario tipoUsuario = new TipoUsuario(UUID.randomUUID(), TipoUsuarioEnum.PACIENTE, true);
        Usuario usuario = new Usuario(id, "Maria", "maria@email.com", "12345678900", "hash", tipoUsuario, true);

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO response = useCase.executar(id);

        assertEquals(id, response.id());
        assertEquals("Maria", response.nome());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        BuscarUsuarioPorIdUseCase useCase = new BuscarUsuarioPorIdUseCase(gateway);
        UUID id = UUID.randomUUID();

        when(gateway.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.executar(id));
    }

    @Test
    void deveListarUsuarios() {
        ListarUsuariosUseCase useCase = new ListarUsuariosUseCase(gateway);
        TipoUsuario tipoUsuario = new TipoUsuario(UUID.randomUUID(), TipoUsuarioEnum.MEDICO, true);
        Usuario usuario = new Usuario(UUID.randomUUID(), "Joao", "joao@email.com", "98765432100", "hash", tipoUsuario, true);

        when(gateway.buscarTodos()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> response = useCase.executar();

        assertEquals(1, response.size());
        assertEquals("Joao", response.get(0).nome());
    }
}
