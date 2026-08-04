package com.techchallenge.user_service.infrastructure.config;

import com.techchallenge.user_service.application.gateway.TipoUsuarioGateway;
import com.techchallenge.user_service.application.gateway.UsuarioGateway;
import com.techchallenge.user_service.application.usecase.auth.RealizarLoginUseCase;
import com.techchallenge.user_service.application.usecase.tipoUsuario.BuscarTipoUsuarioPorIdUseCase;
import com.techchallenge.user_service.application.usecase.tipoUsuario.CriarTipoUsuarioUseCase;
import com.techchallenge.user_service.application.usecase.tipoUsuario.ListarTiposUsuarioUseCase;
import com.techchallenge.user_service.application.usecase.usuario.BuscarUsuarioPorIdUseCase;
import com.techchallenge.user_service.application.usecase.auth.CriarUsuarioUseCase;
import com.techchallenge.user_service.application.usecase.usuario.ListarUsuariosUseCase;
import com.techchallenge.user_service.infrastructure.security.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Bean
    public CriarUsuarioUseCase criarUsuarioUseCase(UsuarioGateway usuarioGateway,
                                                   TipoUsuarioGateway tipoUsuarioGateway,
                                                   PasswordEncoder passwordEncoder) {
        return new CriarUsuarioUseCase(usuarioGateway, tipoUsuarioGateway, passwordEncoder);
    }

    @Bean
    public ListarUsuariosUseCase listarUsuariosUseCase(UsuarioGateway usuarioGateway) {
        return new ListarUsuariosUseCase(usuarioGateway);
    }

    @Bean
    public BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase(UsuarioGateway usuarioGateway) {
        return new BuscarUsuarioPorIdUseCase(usuarioGateway);
    }

    @Bean
    public RealizarLoginUseCase realizarLoginUseCase(AuthenticationManager authenticationManager,
                                                     TokenProvider tokenProvider) {
        return new RealizarLoginUseCase(authenticationManager, tokenProvider);
    }
}
