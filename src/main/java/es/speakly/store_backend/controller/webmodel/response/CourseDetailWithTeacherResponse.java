package es.speakly.store_backend.controller.webmodel.response;

import es.speakly.store_backend.controller.webmodel.response.UserSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CourseDetailWithTeacherResponse(
        Long id,
        String title,
        String description,
        BigDecimal price,
        String language,
        String level,
        UserSummaryResponse teacher,
        int duration,
        LocalDateTime createdAt
) {
}
