package es.speakly.store_backend.controller.webmodel.request;

import es.speakly.store_backend.domain.model.UserRole;
import io.micrometer.common.lang.Nullable;

import java.time.LocalDateTime;

public record UserUpdateNotAdminRequest(
        Long id,
        String username,
        String email,
        String profilePictureUrl,
        @Nullable
        String password,
        LocalDateTime createAt,
        Long[] coursesIds,
        UserRole role
) {
}
