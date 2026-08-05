package com.techchallenge.appointment_service.infrastructure.security;

import java.util.UUID;

public record AuthenticatedUser(
        UUID id,
        String email,
        String role
) {
}
