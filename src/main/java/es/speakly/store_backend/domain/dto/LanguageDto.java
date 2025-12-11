package es.speakly.store_backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LanguageDto(
    Long id,
    @NotNull(message = "Language name cannot be null")
    @Size(min = 2, max = 50, message = "Language name must be between 2 and 50 characters")
    String name,
    @NotNull(message = "Language code cannot be null")
    String code
) {}