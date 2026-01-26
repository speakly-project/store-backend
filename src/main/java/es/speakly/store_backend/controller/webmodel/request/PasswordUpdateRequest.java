package es.speakly.store_backend.controller.webmodel.request;

public record PasswordUpdateRequest(
        String oldPassword,
        String newPassword
) {
}
