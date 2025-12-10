package es.speakly.store_backend.domain.dto;

import java.math.BigDecimal;

public record CourseDto(
        Long id,
        String title,
        String description,
        BigDecimal price,
        String language,
        String level,
        Long teacherId
) {

}