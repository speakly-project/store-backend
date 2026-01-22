package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.CourseFiltersDto;
import es.speakly.store_backend.domain.model.Course;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.CourseRepository;
import es.speakly.store_backend.domain.service.CourseService;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import es.speakly.store_backend.mappers.CourseMapper;
import jakarta.transaction.Transactional;

public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Page<CourseDto> getAll(int pageNumber, int pageSize, CourseFiltersDto filters) {
        validatePagination(pageNumber, pageSize);
        return courseRepository.findAllWithFilters(pageNumber, pageSize, filters);
    }

    @Override
    public CourseDto getById(Long id) {
        return courseRepository.findById(id)
                .map(CourseMapper::fromCourseDtoToCourse)
                .map(CourseMapper::fromCourseToCourseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    @Override
    public CourseDto getByTitle(String title) {
        return courseRepository.findByTitle(title)
                .map(CourseMapper::fromCourseDtoToCourse)
                .map(CourseMapper::fromCourseToCourseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with title: " + title));
    }

    @Override
    @Transactional
    public CourseDto createCourse(CourseDto courseDto) {
        if (courseRepository.findByTitle(courseDto.title()).isPresent()) {
            throw new BusinessException("Course with title '" + courseDto.title() + "' already exists");
        }

        Course course = CourseMapper.fromCourseDtoToCourse(courseDto);

        CourseDto validatedDto = CourseMapper.fromCourseToCourseDto(course);

        return courseRepository.save(validatedDto);
    }

    @Override
    @Transactional
    public CourseDto updateCourse(CourseDto courseDto) {
        courseRepository.findById(courseDto.id())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseDto.id()));

        courseRepository.findByTitle(courseDto.title())
                .filter(c -> !c.id().equals(courseDto.id()))
                .ifPresent(c -> {
                    throw new BusinessException("Course with title '" + courseDto.title() + "' already exists");
                });

        Course course = CourseMapper.fromCourseDtoToCourse(courseDto);

        CourseDto validatedDto = CourseMapper.fromCourseToCourseDto(course);

        return courseRepository.save(validatedDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id " + id + " does not exist"));

        courseRepository.delete(id);
    }

    private void validatePagination(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page number and size must be greater than 0");
        }
    }
}