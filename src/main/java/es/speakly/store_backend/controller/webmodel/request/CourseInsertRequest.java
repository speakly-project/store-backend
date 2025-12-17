package es.speakly.store_backend.controller.webmodel.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CourseInsertRequest(
        String title,
        String description,
        BigDecimal price,
        String language,
        String level,
        Long teacherId,
        int duration,
        LocalDateTime createdAt
) {
    public CourseInsertRequest {
        createdAt = createdAt != null
                ? createdAt
                : LocalDateTime.now();
    }
}


