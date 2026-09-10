package com.techchallenge.user_service.application.usecase.tipoUsuario;

import com.techchallenge.user_service.application.dto.TipoUsuarioRequestDTO;
import com.techchallenge.user_service.application.dto.TipoUsuarioResponseDTO;
import com.techchallenge.user_service.application.gateway.TipoUsuarioGateway;
import com.techchallenge.user_service.domain.entity.TipoUsuario;
import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import com.techchallenge.user_service.infrastructure.exception.BadRequestException;
import com.techchallenge.user_service.infrastructure.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TipoUsuarioUseCaseTest {

    private final TipoUsuarioGateway gateway = mock(TipoUsuarioGateway.class);

    @Test
    void deveCriarTipoUsuarioQuandoNaoExistir() {
        CriarTipoUsuarioUseCase useCase = new CriarTipoUsuarioUseCase(gateway);
        TipoUsuarioRequestDTO request = new TipoUsuarioRequestDTO(TipoUsuarioEnum.ENFERMEIRO);
        TipoUsuario tipoUsuario = new TipoUsuario(UUID.randomUUID(), TipoUsuarioEnum.ENFERMEIRO, true);

        when(gateway.existePorTipo(TipoUsuarioEnum.ENFERMEIRO)).thenReturn(false);
        when(gateway.salvar(any(TipoUsuario.class))).thenReturn(tipoUsuario);

        TipoUsuarioResponseDTO response = useCase.executar(request);

        assertEquals(TipoUsuarioEnum.ENFERMEIRO, response.tipo());
        verify(gateway).salvar(any(TipoUsuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoTipoJaExistir() {
        CriarTipoUsuarioUseCase useCase = new CriarTipoUsuarioUseCase(gateway);
        TipoUsuarioRequestDTO request = new TipoUsuarioRequestDTO(TipoUsuarioEnum.PACIENTE);

        when(gateway.existePorTipo(TipoUsuarioEnum.PACIENTE)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> useCase.executar(request));
    }

    @Test
    void deveBuscarTipoUsuarioPorId() {
        BuscarTipoUsuarioPorIdUseCase useCase = new BuscarTipoUsuarioPorIdUseCase(gateway);
        UUID id = UUID.randomUUID();
        TipoUsuario tipoUsuario = new TipoUsuario(id, TipoUsuarioEnum.MEDICO, true);

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(tipoUsuario));

        TipoUsuarioResponseDTO response = useCase.executar(id);

        assertEquals(id, response.id());
        assertEquals(TipoUsuarioEnum.MEDICO, response.tipo());
    }

    @Test
    void deveLancarExcecaoQuandoTipoUsuarioNaoExistir() {
        BuscarTipoUsuarioPorIdUseCase useCase = new BuscarTipoUsuarioPorIdUseCase(gateway);
        UUID id = UUID.randomUUID();

        when(gateway.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.executar(id));
    }

    @Test
    void deveListarTiposUsuario() {
        ListarTiposUsuarioUseCase useCase = new ListarTiposUsuarioUseCase(gateway);
        TipoUsuario tipoUsuario = new TipoUsuario(UUID.randomUUID(), TipoUsuarioEnum.PACIENTE, true);
        when(gateway.buscarTodos()).thenReturn(List.of(tipoUsuario));

        List<TipoUsuarioResponseDTO> response = useCase.executar();

        assertEquals(1, response.size());
        assertEquals(tipoUsuario.getTipo(), response.get(0).tipo());
    }
}
