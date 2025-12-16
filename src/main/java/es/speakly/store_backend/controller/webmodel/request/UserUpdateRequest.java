package es.speakly.store_backend.controller.webmodel.request;


import es.speakly.store_backend.domain.model.UserRole;

import java.time.LocalDateTime;

public record UserUpdateRequest(
        Long id,
        String username,
        String email,
        String profilePictureUrl,
        String password,
        LocalDateTime createAt,
        Long[] coursesIds,
        UserRole role

) {

}
