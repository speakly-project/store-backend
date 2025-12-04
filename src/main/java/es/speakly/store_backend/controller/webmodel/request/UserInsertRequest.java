package es.speakly.store_backend.controller.webmodel.request;

import java.time.LocalDateTime;

public record UserInsertRequest(
    String username,
    String email,
    String password,
    String profilePictureUrl,
    LocalDateTime createdAt,
    Long[] coursesTakenIds
) {

}
