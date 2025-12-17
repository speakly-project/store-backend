package es.speakly.store_backend.controller.webmodel.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CourseSummaryResponse(
        String title,
        String description,
        BigDecimal price,
        String language,
        String level,
        int duration,
        LocalDateTime createdAt
) {
}
