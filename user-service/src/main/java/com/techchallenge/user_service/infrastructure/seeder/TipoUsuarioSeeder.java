package com.techchallenge.user_service.infrastructure.seeder;

import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import com.techchallenge.user_service.infrastructure.database.entity.TipoUsuarioEntity;
import com.techchallenge.user_service.infrastructure.database.repository.TipoUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TipoUsuarioSeeder implements CommandLineRunner {

    private final TipoUsuarioRepository repository;

    @Override
    public void run(String... args) throws Exception {
        for (TipoUsuarioEnum tipo : TipoUsuarioEnum.values()) {
            criarSeNaoExistir(tipo);
        }
    }

    private void criarSeNaoExistir(TipoUsuarioEnum tipo) {
        if (repository.existsByTipo(tipo)) {
            return;
        }

        repository.save(
                new TipoUsuarioEntity(
                        null,
                        tipo,
                        true
                )
        );
    }
}
