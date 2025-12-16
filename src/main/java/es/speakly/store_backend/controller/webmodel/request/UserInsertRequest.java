package es.speakly.store_backend.controller.webmodel.request;


import es.speakly.store_backend.domain.model.UserRole;

import java.time.LocalDateTime;

public record UserInsertRequest(
    String username,
    String email,
    String profilePictureUrl,
    String password,
    LocalDateTime createdAt,
    Long[] coursesTakenIds,
    UserRole role
) {
    public UserInsertRequest {
        createdAt = createdAt != null
                ? createdAt
                : LocalDateTime.now();
    }


}
