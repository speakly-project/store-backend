package es.speakly.store_backend.domain.service;

import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.model.Page;

import java.util.Optional;

public interface CourseService {
    Page<CourseDto> getAll(int pageNumber, int pageSize);
    CourseDto getById(Long id);
    CourseDto getByTitle(String title);
    Page<CourseDto> getByLanguageAndLevel(String language, String level, int pageNumber, int pageSize);
    CourseDto createCourse(CourseDto course);
    CourseDto updateCourse(CourseDto course);
    void delete(Long id);
}
