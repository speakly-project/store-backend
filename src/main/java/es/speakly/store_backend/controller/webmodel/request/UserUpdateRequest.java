package es.speakly.store_backend.controller.webmodel.request;


import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import es.speakly.store_backend.domain.model.UserRole;
import io.micrometer.common.lang.Nullable;

import java.time.LocalDateTime;

public record UserUpdateRequest(
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
