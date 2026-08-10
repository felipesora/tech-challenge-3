package com.techchallenge.user_service.infrastructure.security;

import com.techchallenge.user_service.domain.entity.TipoUsuario;
import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import com.techchallenge.user_service.domain.entity.Usuario;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDetailsAdapterTest {

    private final UUID usuarioId = UUID.randomUUID();
    private final TipoUsuario tipoUsuario = new TipoUsuario(UUID.randomUUID(), TipoUsuarioEnum.MEDICO, true);

    private Usuario criarUsuario(boolean ativo) {
        return new Usuario(usuarioId, "Maria", "maria@email.com", "12345678900", "hash-senha", tipoUsuario, ativo);
    }

    @Test
    void deveExporIdDoUsuario() {
        UserDetailsAdapter adapter = new UserDetailsAdapter(criarUsuario(true));

        assertEquals(usuarioId, adapter.getId());
    }

    @Test
    void deveExporUsuarioOriginal() {
        Usuario usuario = criarUsuario(true);
        UserDetailsAdapter adapter = new UserDetailsAdapter(usuario);

        assertEquals(usuario, adapter.getUsuario());
    }

    @Test
    void deveExporAuthorityComPrefixoRole() {
        UserDetailsAdapter adapter = new UserDetailsAdapter(criarUsuario(true));

        assertTrue(adapter.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MEDICO")));
    }

    @Test
    void deveExporSenhaHashComoPassword() {
        UserDetailsAdapter adapter = new UserDetailsAdapter(criarUsuario(true));

        assertEquals("hash-senha", adapter.getPassword());
    }

    @Test
    void deveExporEmailComoUsername() {
        UserDetailsAdapter adapter = new UserDetailsAdapter(criarUsuario(true));

        assertEquals("maria@email.com", adapter.getUsername());
    }

    @Test
    void deveConsiderarContaSempreNaoExpiradaENaoBloqueada() {
        UserDetailsAdapter adapter = new UserDetailsAdapter(criarUsuario(true));

        assertTrue(adapter.isAccountNonExpired());
        assertTrue(adapter.isAccountNonLocked());
        assertTrue(adapter.isCredentialsNonExpired());
    }

    @Test
    void deveRefletirAtivoDoUsuarioNoIsEnabled() {
        UserDetailsAdapter ativo = new UserDetailsAdapter(criarUsuario(true));
        UserDetailsAdapter inativo = new UserDetailsAdapter(criarUsuario(false));

        assertTrue(ativo.isEnabled());
        assertFalse(inativo.isEnabled());
    }
}
