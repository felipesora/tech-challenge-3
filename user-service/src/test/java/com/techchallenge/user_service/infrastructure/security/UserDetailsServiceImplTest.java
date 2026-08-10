package com.techchallenge.user_service.infrastructure.security;

import com.techchallenge.user_service.application.gateway.UsuarioGateway;
import com.techchallenge.user_service.domain.entity.TipoUsuario;
import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import com.techchallenge.user_service.domain.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    private final UsuarioGateway usuarioGateway = mock(UsuarioGateway.class);
    private final UserDetailsServiceImpl service = new UserDetailsServiceImpl(usuarioGateway);

    @Test
    void deveCarregarUsuarioPorEmail() {
        TipoUsuario tipoUsuario = new TipoUsuario(UUID.randomUUID(), TipoUsuarioEnum.PACIENTE, true);
        Usuario usuario = new Usuario(UUID.randomUUID(), "Maria", "maria@email.com", "12345678900", "hash", tipoUsuario, true);
        when(usuarioGateway.buscarPorEmail("maria@email.com")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = service.loadUserByUsername("maria@email.com");

        assertInstanceOf(UserDetailsAdapter.class, userDetails);
        assertEquals("maria@email.com", userDetails.getUsername());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {
        when(usuarioGateway.buscarPorEmail("inexistente@email.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("inexistente@email.com"));
    }
}
