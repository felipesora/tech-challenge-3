package com.techchallenge.user_service.infrastructure.seeder;

import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import com.techchallenge.user_service.infrastructure.database.entity.TipoUsuarioEntity;
import com.techchallenge.user_service.infrastructure.database.repository.TipoUsuarioRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TipoUsuarioSeederTest {

    private final TipoUsuarioRepository repository = mock(TipoUsuarioRepository.class);
    private final TipoUsuarioSeeder seeder = new TipoUsuarioSeeder(repository);

    @Test
    void deveCriarTodosOsTiposQuandoNenhumExistir() throws Exception {
        when(repository.existsByTipo(any(TipoUsuarioEnum.class))).thenReturn(false);

        seeder.run();

        verify(repository, times(TipoUsuarioEnum.values().length)).save(any(TipoUsuarioEntity.class));
    }

    @Test
    void naoDeveCriarTiposQueJaExistem() throws Exception {
        when(repository.existsByTipo(any(TipoUsuarioEnum.class))).thenReturn(true);

        seeder.run();

        verify(repository, never()).save(any(TipoUsuarioEntity.class));
    }
}
