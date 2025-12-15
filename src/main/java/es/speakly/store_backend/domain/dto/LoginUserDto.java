package es.speakly.store_backend.domain.dto;

public record LoginUserDto(
        Long id,
        String email
        // String role
) {
}
