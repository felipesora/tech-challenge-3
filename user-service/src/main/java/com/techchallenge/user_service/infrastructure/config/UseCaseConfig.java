package com.techchallenge.user_service.infrastructure.config;

import com.techchallenge.user_service.application.gateway.TipoUsuarioGateway;
import com.techchallenge.user_service.application.usecase.tipoUsuario.BuscarTipoUsuarioPorIdUseCase;
import com.techchallenge.user_service.application.usecase.tipoUsuario.CriarTipoUsuarioUseCase;
import com.techchallenge.user_service.application.usecase.tipoUsuario.ListarTiposUsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CriarTipoUsuarioUseCase criarTipoUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new CriarTipoUsuarioUseCase(gateway);
    }

    @Bean
    public ListarTiposUsuarioUseCase listarTiposUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new ListarTiposUsuarioUseCase(gateway);
    }

    @Bean
    public BuscarTipoUsuarioPorIdUseCase buscarTipoUsuarioPorIdUseCase(TipoUsuarioGateway gateway) {
        return new BuscarTipoUsuarioPorIdUseCase(gateway);
    }
}
