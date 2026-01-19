package es.speakly.store_backend.controller.webmodel.request;

public record LoginRequest(
        String username,
        String password
) {
}
