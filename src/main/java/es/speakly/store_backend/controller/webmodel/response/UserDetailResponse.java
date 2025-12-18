package es.speakly.store_backend.controller.webmodel.response;


import es.speakly.store_backend.domain.model.UserRole;

import java.time.LocalDateTime;
import java.util.List;

public record UserDetailResponse(
    Long id,
    String username,
    String email,
    String profilePictureUrl,
    LocalDateTime createdAt,
    List<CourseSummaryResponse> coursesTaken,
    UserRole role
) {

}
