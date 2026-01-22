package es.speakly.store_backend.domain.repository;

import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.CourseFiltersDto;
import es.speakly.store_backend.domain.model.Page;

import java.util.Optional;

public interface CourseRepository {
    Page<CourseDto> findAll(int pageNumber, int pageSize);
    Page<CourseDto> findAllWithFilters(int pageNumber, int pageSize, CourseFiltersDto courseFiltersDto);
    Optional<CourseDto> findById(Long id);
    Optional<CourseDto> findByTitle(String title);
    CourseDto save(CourseDto course);
    void delete(Long id);
}
