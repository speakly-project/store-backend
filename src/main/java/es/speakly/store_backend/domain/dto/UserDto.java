package es.speakly.store_backend.domain.dto;


import es.speakly.store_backend.domain.model.UserRole;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto(
    Long id,
    @NotNull(message = "Username cannot be null")
    String username,
    @NotNull(message = "Email cannot be null")
    String email,
    String profilePictureUrl,
    @NotNull(message = "Password cannot be null")
    String password,
    LocalDateTime createdAt,
    List<CourseDto> coursesTaken,
    UserRole role
) {
    public UserDto {
        if (coursesTaken == null || coursesTaken.isEmpty()) {
            coursesTaken = List.of();
        } else {
            coursesTaken = List.copyOf(coursesTaken);
        }
    }
}