package es.speakly.store_backend.domain.dto;

import es.speakly.store_backend.domain.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginUserDto(
        Long id,
        @NotBlank(message = "Email cannot be null or empty")
        String email,
        @NotNull
        UserRole role
) {
}
