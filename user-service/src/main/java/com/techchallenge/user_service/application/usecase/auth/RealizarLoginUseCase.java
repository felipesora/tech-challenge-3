package com.techchallenge.user_service.application.usecase.auth;

import com.techchallenge.user_service.application.dto.LoginRequestDTO;
import com.techchallenge.user_service.application.dto.TokenResponseDTO;
import com.techchallenge.user_service.infrastructure.security.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class RealizarLoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public RealizarLoginUseCase(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public TokenResponseDTO execute(LoginRequestDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));
            String token = tokenProvider.gerarToken(authentication);

            return new TokenResponseDTO(token, expirationTime);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciais inválidas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
