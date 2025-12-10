package es.speakly.store_backend.domain.dto;


import java.time.LocalDateTime;
import java.util.List;

public record UserDto(
    Long id,
    String username,
    String email,
    String profilePictureUrl,
    String password,
    LocalDateTime createdAt,
    List<CourseDto> coursesTaken
) {
}