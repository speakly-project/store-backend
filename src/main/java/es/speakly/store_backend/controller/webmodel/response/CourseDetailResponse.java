package es.speakly.store_backend.controller.webmodel.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CourseDetailResponse(
        Long id,
        String title,
        String description,
        BigDecimal price,
        String language,
        String level,
        Long teacherId,
        int duration,
        LocalDateTime createdAt
) {
}
