package es.speakly.store_backend.controller.webmodel.response;


import java.time.LocalDateTime;
import java.util.List;

public record UserDetailResponse(
    Long id,
    String username,
    String email,
    String password,
    String profilePictureUrl,
    LocalDateTime createdAt,
    List<CourseSummaryResponse> coursesTaken
) {

}
