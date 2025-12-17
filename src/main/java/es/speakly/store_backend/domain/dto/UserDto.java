package es.speakly.store_backend.domain.dto;


import es.speakly.store_backend.domain.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto(
    Long id,
    @NotBlank(message = "Username cannot be null or empty")
    String username,
    @NotBlank(message = "Email cannot be null or empty")
    String email,
    String profilePictureUrl,
    @NotBlank(message = "Password cannot be null or empty")
    String password,
    LocalDateTime createdAt,
    List<CourseDto> coursesTaken,
    @NotNull
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