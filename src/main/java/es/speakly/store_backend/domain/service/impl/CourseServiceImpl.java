package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.model.Course;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.CourseRepository;
import es.speakly.store_backend.domain.service.CourseService;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import es.speakly.store_backend.mappers.CourseMapper;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Page<CourseDto> getAll(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page number and size must be greater than 0");
        }
        Page<CourseDto> courseEntityPage = courseRepository.findAll(pageNumber, pageSize);
        List<CourseDto> itemsDto = courseEntityPage.data()
                .stream()
                .map(CourseMapper::fromCourseDtoToCourse)
                .map(CourseMapper::fromCourseToCourseDto)
                .toList();
        return new Page<>(
                itemsDto,
                courseEntityPage.pageNumber(),
                courseEntityPage.pageSize(),
                courseEntityPage.totalElements()
        );
    }

    @Override
    public CourseDto getById(Long id) {
        return courseRepository.findById(id)
                .map(CourseMapper::fromCourseDtoToCourse)
                .map(CourseMapper::fromCourseToCourseDto).orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    @Override
    public CourseDto getByTitle(String title) {
        return courseRepository.findByTitle(title)
                .map(CourseMapper::fromCourseDtoToCourse)
                .map(CourseMapper::fromCourseToCourseDto).orElseThrow(() -> new ResourceNotFoundException("Course not found with title: " + title));
    }

    @Override
    public Page<CourseDto> getByLanguageAndLevel(String language, String level, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page number and size must be greater than 0");
        }
        return courseRepository.findByLanguageAndLevel(language, level, pageNumber, pageSize);
    }

    @Override
    @Transactional
    public CourseDto createCourse(CourseDto course) {
        if (courseRepository.findByTitle(course.title()).isPresent()) {
            throw new BusinessException("Course with title " + course.title() + " already exists");
        }
        Course newCourse = CourseMapper.fromCourseDtoToCourse(course);
        CourseDto courseDto = CourseMapper.fromCourseToCourseDto(newCourse);
        return courseRepository.save(courseDto);
    }

    @Override
    @Transactional
    public CourseDto updateCourse(CourseDto course) {
        courseRepository.findById(course.id())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + course.id()));
        courseRepository.findByTitle(course.title())
                .filter(c -> !c.id().equals(course.id()))
                .ifPresent(c -> {
                    throw new BusinessException("Course with title " + course.title() + " already exists");
                });
        Course newCourse = CourseMapper.fromCourseDtoToCourse(course);
        CourseDto courseDto = CourseMapper.fromCourseToCourseDto(newCourse);
        return courseRepository.save(courseDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<CourseDto> courseDto = courseRepository.findById(id);
        if (courseDto.isEmpty()) {
            throw new ResourceNotFoundException("Course with id " + id + " does not exist");
        }
        courseRepository.delete(id);
    }
}
