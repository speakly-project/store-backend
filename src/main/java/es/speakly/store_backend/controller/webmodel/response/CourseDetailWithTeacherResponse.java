package es.speakly.store_backend.controller.webmodel.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CourseDetailWithTeacherResponse(
        Long id,
        String title,
        String description,
        BigDecimal price,
        String language,
        String level,
        UserDetailResponse teacher,
        int duration,
        LocalDateTime createdAt
) {
}

