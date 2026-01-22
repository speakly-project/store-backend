package es.speakly.store_backend.controller.webmodel.response;

public record UserSummaryResponse(
        String username,
        String email,
        String profilePictureUrl
) {
}
