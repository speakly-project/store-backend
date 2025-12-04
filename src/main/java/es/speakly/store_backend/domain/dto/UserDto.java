package es.speakly.store_backend.domain.dto;

import es.speakly.store_backend.domain.model.Course;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto(
    Long id,
    String username,
    String email,
    String password,
    String profilePictureUrl,
    LocalDateTime createdAt,
    List<Course> coursesTaken
) {
}