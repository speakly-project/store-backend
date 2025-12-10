package es.speakly.store_backend.controller.webmodel.request;

import java.math.BigDecimal;

public record CourseInsertRequest(
        String title,
        String description,
        BigDecimal price,
        String language,
        String level,
        Long teacherId
) {
}


