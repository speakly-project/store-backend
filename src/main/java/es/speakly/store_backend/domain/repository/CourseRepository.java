package es.speakly.store_backend.domain.repository;

import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.model.Page;

import java.util.Optional;

public interface CourseRepository {
    Page<CourseDto> findAll(int pageNumber, int pageSize);
    Optional<CourseDto> findById(Long id);
    Optional<CourseDto> findByTitle(String title);
    Page<CourseDto> findByLanguageAndLevel(String language, String level, int pageNumber, int pageSize);
    CourseDto save(CourseDto course);
    void delete(Long id);
}
