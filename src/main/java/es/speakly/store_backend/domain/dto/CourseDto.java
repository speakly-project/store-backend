package es.speakly.store_backend.domain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CourseDto(
        Long id,
        @NotBlank(message = "Username cannot be null or empty")
        String title,
        String description,
        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "1.0", message = "Price must be non-negative")
        BigDecimal price,
        @NotBlank(message = "Language cannot be null or empty")
        String language,
        @NotBlank(message = "Level cannot be null or empty")
        String level,
        UserDto teacher,
        @NotNull
        int duration,
        LocalDateTime createdAt
) {

}