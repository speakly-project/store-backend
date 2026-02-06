package es.speakly.store_backend.controller.webmodel.response;

import es.speakly.store_backend.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        OrderStatus orderStatus,
        List<OrderItemResponse> orderItems,
        BigDecimal totalPrice,
        LocalDateTime createdAt
) {
}
