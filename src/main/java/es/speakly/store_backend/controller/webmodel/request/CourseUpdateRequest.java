package es.speakly.store_backend.controller.webmodel.request;

import java.math.BigDecimal;

public record CourseUpdateRequest(
        Long id,
        String title,
        String description,
        BigDecimal price,
        String language,
        String level,
        Long teacherId,
        int duration
) {
}

