package es.speakly.store_backend.domain.dto;

import es.speakly.store_backend.domain.model.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,
        @NotNull(message = "User cannot be null")
        UserDto user,
        @NotNull(message = "Order status cannot be null")
        OrderStatus status,
        List<OrderItemDto> items,
        @Min(value = 0, message = "Total price must be non-negative")
        BigDecimal totalPrice,
        LocalDateTime paidDate,
        LocalDateTime createdAt
) {
}
