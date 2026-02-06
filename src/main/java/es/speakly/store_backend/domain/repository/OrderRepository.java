package es.speakly.store_backend.domain.repository;

import es.speakly.store_backend.domain.dto.OrderDto;
import es.speakly.store_backend.domain.model.OrderStatus;

import java.util.Optional;

public interface OrderRepository {
    OrderDto save(OrderDto orderDto);
    void delete(Long orderId);
    Optional<OrderStatus> getStatus(Long orderId);
    Optional<OrderDto> getActiveOrder(Long userId);
}
