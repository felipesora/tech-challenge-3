package com.techchallenge.user_service.application.dto;

public record TokenResponseDTO(String token, long expiresIn) {
}
