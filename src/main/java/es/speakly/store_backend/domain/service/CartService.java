package es.speakly.store_backend.domain.service;

import es.speakly.store_backend.domain.dto.OrderDto;
import es.speakly.store_backend.domain.model.OrderStatus;

public interface CartService {
    void deleteCart(Long id);
    void createCart(Long id);
    OrderDto getCart(Long id);
    OrderStatus getCartStatus(Long id);
    void updatePendingCart(OrderDto orderDto);
    void updateCart(OrderDto orderDto);
    void payCart(Long userId, String cardNumber, String expiryDate, String cvv, String fullName);
}
