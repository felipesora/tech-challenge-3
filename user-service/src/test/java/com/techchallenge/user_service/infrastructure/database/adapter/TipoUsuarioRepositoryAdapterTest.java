package com.techchallenge.user_service.infrastructure.database.adapter;

import com.techchallenge.user_service.domain.entity.TipoUsuario;
import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import com.techchallenge.user_service.infrastructure.database.entity.TipoUsuarioEntity;
import com.techchallenge.user_service.infrastructure.database.repository.TipoUsuarioRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TipoUsuarioRepositoryAdapterTest {

    private final TipoUsuarioRepository repository = mock(TipoUsuarioRepository.class);
    private final TipoUsuarioRepositoryAdapter adapter = new TipoUsuarioRepositoryAdapter(repository);

    @Test
    void deveSalvarTipoUsuarioConvertendoParaDominio() {
        TipoUsuario domain = new TipoUsuario(UUID.randomUUID(), TipoUsuarioEnum.MEDICO, true);
        TipoUsuarioEntity entity = new TipoUsuarioEntity(domain.getId(), domain.getTipo(), domain.getAtivo());
        when(repository.save(any(TipoUsuarioEntity.class))).thenReturn(entity);

        TipoUsuario resultado = adapter.salvar(domain);

        assertEquals(domain.getTipo(), resultado.getTipo());
        verify(repository).save(any(TipoUsuarioEntity.class));
    }

    @Test
    void deveBuscarTodosTiposUsuario() {
        TipoUsuarioEntity entity = new TipoUsuarioEntity(UUID.randomUUID(), TipoUsuarioEnum.PACIENTE, true);
        when(repository.findAll()).thenReturn(List.of(entity));

        List<TipoUsuario> resultado = adapter.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals(entity.getTipo(), resultado.get(0).getTipo());
    }

    @Test
    void deveBuscarPorIdEExistencia() {
        UUID id = UUID.randomUUID();
        TipoUsuarioEntity entity = new TipoUsuarioEntity(id, TipoUsuarioEnum.ENFERMEIRO, true);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.existsByTipo(TipoUsuarioEnum.ENFERMEIRO)).thenReturn(true);

        Optional<TipoUsuario> resultado = adapter.buscarPorId(id);
        boolean existe = adapter.existePorTipo(TipoUsuarioEnum.ENFERMEIRO);

        assertTrue(resultado.isPresent());
        assertTrue(existe);
    }
}
