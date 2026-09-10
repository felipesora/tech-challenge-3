package com.techchallenge.user_service.application.usecase.auth;

import com.techchallenge.user_service.application.dto.LoginRequestDTO;
import com.techchallenge.user_service.application.dto.TokenResponseDTO;
import com.techchallenge.user_service.application.dto.UsuarioRequestDTO;
import com.techchallenge.user_service.application.dto.UsuarioResponseDTO;
import com.techchallenge.user_service.application.gateway.TipoUsuarioGateway;
import com.techchallenge.user_service.application.gateway.UsuarioGateway;
import com.techchallenge.user_service.domain.entity.TipoUsuario;
import com.techchallenge.user_service.domain.entity.TipoUsuarioEnum;
import com.techchallenge.user_service.domain.entity.Usuario;
import com.techchallenge.user_service.infrastructure.exception.BadRequestException;
import com.techchallenge.user_service.infrastructure.exception.EntityNotFoundException;
import com.techchallenge.user_service.infrastructure.security.TokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioAuthUseCaseTest {

    private final UsuarioGateway usuarioGateway = mock(UsuarioGateway.class);
    private final TipoUsuarioGateway tipoUsuarioGateway = mock(TipoUsuarioGateway.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final CriarUsuarioUseCase criarUsuarioUseCase = new CriarUsuarioUseCase(usuarioGateway, tipoUsuarioGateway, passwordEncoder);

    @Test
    void deveCriarUsuarioQuandoDadosForemValidos() {
        UUID tipoId = UUID.randomUUID();
        TipoUsuario tipoUsuario = new TipoUsuario(tipoId, TipoUsuarioEnum.PACIENTE, true);
        UsuarioRequestDTO request = new UsuarioRequestDTO("Maria", "maria@email.com", "123.456.789-00", "123456", tipoId);
        Usuario usuarioSalvo = new Usuario(UUID.randomUUID(), "Maria", "maria@email.com", "12345678900", "hash", tipoUsuario, true);

        when(usuarioGateway.existePorEmail("maria@email.com")).thenReturn(false);
        when(usuarioGateway.existePorCpf("12345678900")).thenReturn(false);
        when(tipoUsuarioGateway.buscarPorId(tipoId)).thenReturn(Optional.of(tipoUsuario));
        when(passwordEncoder.encode("123456")).thenReturn("hash");
        when(usuarioGateway.salvar(any(Usuario.class))).thenReturn(usuarioSalvo);

        UsuarioResponseDTO response = criarUsuarioUseCase.executar(request);

        assertEquals("maria@email.com", response.email());
        assertEquals(tipoId, response.tipoUsuarioId());

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioGateway).salvar(captor.capture());
        assertEquals("12345678900", captor.getValue().getCpf());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExistir() {
        UUID tipoId = UUID.randomUUID();
        UsuarioRequestDTO request = new UsuarioRequestDTO("Maria", "maria@email.com", "123.456.789-00", "123456", tipoId);

        when(usuarioGateway.existePorEmail("maria@email.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> criarUsuarioUseCase.executar(request));
    }

    @Test
    void deveLancarExcecaoQuandoTipoUsuarioNaoExistir() {
        UUID tipoId = UUID.randomUUID();
        UsuarioRequestDTO request = new UsuarioRequestDTO("Maria", "maria@email.com", "123.456.789-00", "123456", tipoId);

        when(usuarioGateway.existePorEmail("maria@email.com")).thenReturn(false);
        when(usuarioGateway.existePorCpf("12345678900")).thenReturn(false);
        when(tipoUsuarioGateway.buscarPorId(tipoId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> criarUsuarioUseCase.executar(request));
    }

    @Test
    void deveRealizarLoginEGerarToken() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        TokenProvider tokenProvider = mock(TokenProvider.class);
        RealizarLoginUseCase useCase = new RealizarLoginUseCase(authenticationManager, tokenProvider);
        LoginRequestDTO request = new LoginRequestDTO("maria@email.com", "123456");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("maria@email.com", "123456");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenProvider.gerarToken(authentication)).thenReturn("token-123");

        TokenResponseDTO response = useCase.execute(request);

        assertEquals("token-123", response.token());
    }

    @Test
    void deveLancarExcecaoQuandoCredenciaisForemInvalidas() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        TokenProvider tokenProvider = mock(TokenProvider.class);
        RealizarLoginUseCase useCase = new RealizarLoginUseCase(authenticationManager, tokenProvider);
        LoginRequestDTO request = new LoginRequestDTO("maria@email.com", "123456");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThrows(BadCredentialsException.class, () -> useCase.execute(request));
    }
}
