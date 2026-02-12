package es.speakly.store_backend.nanoServices.payment.dtos;

public record AuthorizationDto(
        String username,
        String apiKey
) {
}
