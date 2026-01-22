package es.speakly.store_backend.domain.service;

import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.CourseFiltersDto;
import es.speakly.store_backend.domain.model.Page;

public interface CourseService {
    Page<CourseDto> getAll(int pageNumber, int pageSize, CourseFiltersDto courseFiltersDto);
    CourseDto getById(Long id);
    CourseDto getByTitle(String title);
    CourseDto createCourse(CourseDto course);
    CourseDto updateCourse(CourseDto course);
    void delete(Long id);
}
