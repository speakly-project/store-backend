package es.speakly.store_backend.nanoServices.payment.dtos;

public record OriginDto (
        String cardNumber,
        String expiryDate,
        String cvv,
        String fullName
) {
}
