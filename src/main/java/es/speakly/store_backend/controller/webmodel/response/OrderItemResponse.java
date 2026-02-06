package es.speakly.store_backend.controller.webmodel.response;

import java.math.BigDecimal;

public record OrderItemResponse (
        Long id,
        CourseSummaryResponse course,
        Long quantity,
        BigDecimal price
){
}
