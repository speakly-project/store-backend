package es.speakly.store_backend.controller.webmodel.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CartPaymentRequest(
        @NotNull(message = "User ID cannot be null")
        Long userId,
        @NotBlank(message = "Card number cannot be blank")
        String cardNumber,
        @NotBlank(message = "Expiry date cannot be blank")
        String expiryDate,
        @NotBlank(message = "CVV cannot be blank")
        String cvv,
        @NotBlank(message = "Full name cannot be blank")
        String fullName
) {
}
