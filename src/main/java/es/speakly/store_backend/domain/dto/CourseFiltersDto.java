package es.speakly.store_backend.domain.dto;

public record CourseFiltersDto (
    String language,
    String level,
    Integer minPrice,
    Integer maxPrice,
    String sortBy
){}
