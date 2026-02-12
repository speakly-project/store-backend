package es.speakly.store_backend.controller.webmodel.response;

import java.math.BigDecimal;

public record CartPaymentResponse(
        Long orderId,
        String paymentStatus,
        BigDecimal totalPaid,
        String message
) {
}
