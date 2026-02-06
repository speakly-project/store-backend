package es.speakly.store_backend.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemDto(
        Long id,
        CourseDto course,
        @Min(value = 1, message = "Quantity must be at least 1")
        Long quantity,
        @NotNull
        @Min(value = 0, message = "Price must be non-negative")
        BigDecimal price
) {
}
