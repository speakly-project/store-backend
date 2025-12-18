package es.speakly.store_backend.controller.webmodel.response;

import java.time.LocalDateTime;

public record UserSummaryResponse(
        String username,
        String email,
        String profilePictureUrl,
        LocalDateTime createdAt

) {
}
