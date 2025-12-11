package es.speakly.store_backend.domain.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public record LevelDto(
    Long id,
    @NotBlank(message = "Level name cannot be null")
    @Size(min = 1, max = 20, message = "Level name must be between 1 and 20 characters")
    String name
) {}
