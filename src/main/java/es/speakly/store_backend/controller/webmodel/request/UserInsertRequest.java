package es.speakly.store_backend.controller.webmodel.request;

import java.time.LocalDateTime;

public record UserInsertRequest(
    String username,
    String email,
    String profilePictureUrl,
    String password,
    LocalDateTime createdAt,
    Long[] coursesTakenIds
) {

}
