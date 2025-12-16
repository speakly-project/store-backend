package es.speakly.store_backend.domain.dto;

import es.speakly.store_backend.domain.model.UserRole;

public record LoginUserDto(
        Long id,
        String email,
        UserRole role
) {
}
