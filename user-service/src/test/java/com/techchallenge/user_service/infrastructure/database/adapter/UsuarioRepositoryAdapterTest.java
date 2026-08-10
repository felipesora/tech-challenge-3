package com.techchallenge.user_service.infrastructure.database.adapter;

import com.techchallenge.user_service.domain.entity.TipoUsuario;
import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import com.techchallenge.user_service.domain.entity.Usuario;
import com.techchallenge.user_service.infrastructure.database.entity.TipoUsuarioEntity;
import com.techchallenge.user_service.infrastructure.database.entity.UsuarioEntity;
import com.techchallenge.user_service.infrastructure.database.repository.UsuarioRepository;
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

class UsuarioRepositoryAdapterTest {

    private final UsuarioRepository repository = mock(UsuarioRepository.class);
    private final UsuarioRepositoryAdapter adapter = new UsuarioRepositoryAdapter(repository);

    @Test
    void deveSalvarUsuarioConvertendoParaDominio() {
        TipoUsuario tipoUsuario = new TipoUsuario(UUID.randomUUID(), TipoUsuarioEnum.PACIENTE, true);
        Usuario domain = new Usuario(UUID.randomUUID(), "Maria", "maria@email.com", "12345678900", "hash", tipoUsuario, true);
        UsuarioEntity entity = new UsuarioEntity(domain.getId(), domain.getNome(), domain.getEmail(), domain.getCpf(), domain.getSenhaHash(), new TipoUsuarioEntity(tipoUsuario.getId(), tipoUsuario.getTipo(), tipoUsuario.getAtivo()), domain.getAtivo());
        when(repository.save(any(UsuarioEntity.class))).thenReturn(entity);

        Usuario resultado = adapter.salvar(domain);

        assertEquals(domain.getEmail(), resultado.getEmail());
        assertEquals(domain.getCpf(), resultado.getCpf());
        verify(repository).save(any(UsuarioEntity.class));
    }

    @Test
    void deveBuscarTodosUsuarios() {
        TipoUsuarioEntity tipoUsuarioEntity = new TipoUsuarioEntity(UUID.randomUUID(), TipoUsuarioEnum.MEDICO, true);
        UsuarioEntity entity = new UsuarioEntity(UUID.randomUUID(), "Joao", "joao@email.com", "98765432100", "hash", tipoUsuarioEntity, true);
        when(repository.findAll()).thenReturn(List.of(entity));

        List<Usuario> resultado = adapter.buscarTodos();

        assertEquals(1, resultado.size());
        assertEquals(entity.getEmail(), resultado.get(0).getEmail());
    }

    @Test
    void deveBuscarPorIdEEmail() {
        UUID id = UUID.randomUUID();
        TipoUsuarioEntity tipoUsuarioEntity = new TipoUsuarioEntity(UUID.randomUUID(), TipoUsuarioEnum.ENFERMEIRO, true);
        UsuarioEntity entity = new UsuarioEntity(id, "Ana", "ana@email.com", "11122233344", "hash", tipoUsuarioEntity, true);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.findByEmail("ana@email.com")).thenReturn(Optional.of(entity));
        when(repository.existsByEmail("ana@email.com")).thenReturn(true);
        when(repository.existsByCpf("11122233344")).thenReturn(true);

        Optional<Usuario> porId = adapter.buscarPorId(id);
        Optional<Usuario> porEmail = adapter.buscarPorEmail("ana@email.com");
        boolean existeEmail = adapter.existePorEmail("ana@email.com");
        boolean existeCpf = adapter.existePorCpf("11122233344");

        assertTrue(porId.isPresent());
        assertTrue(porEmail.isPresent());
        assertTrue(existeEmail);
        assertTrue(existeCpf);
    }
}
