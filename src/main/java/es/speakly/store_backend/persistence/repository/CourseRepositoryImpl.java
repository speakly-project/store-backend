package es.speakly.store_backend.persistence.repository;

import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.CourseFiltersDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.CourseRepository;
import es.speakly.store_backend.mappers.CourseMapper;
import es.speakly.store_backend.persistence.dao.CourseDao;
import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;

import java.util.List;
import java.util.Optional;

public class CourseRepositoryImpl implements CourseRepository {
    private final CourseDao courseDao;

    public CourseRepositoryImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public Page<CourseDto> findAllWithFilters(int pageNumber, int pageSize, CourseFiltersDto filters) {
        List<CourseJpaEntity> entities = courseDao.findAllWithFilters(pageNumber, pageSize, filters);
        List<CourseDto> dtos = mapToDtos(entities);
        long totalElements = courseDao.countWithFilters(filters);
        return new Page<>(dtos, pageNumber, pageSize, totalElements);
    }

    @Override
    public Page<CourseDto> findAll(int pageNumber, int pageSize) {
        return findAllWithFilters(pageNumber, pageSize, null);
    }

    @Override
    public Optional<CourseDto> findById(Long id) {
        return courseDao.findById(id)
                .map(CourseMapper::fromCourseEntityToCourseDto);
    }

    @Override
    public Optional<CourseDto> findByTitle(String title) {
        return courseDao.findByTitle(title)
                .map(CourseMapper::fromCourseEntityToCourseDto);
    }

    @Override
    public CourseDto save(CourseDto course) {
        CourseJpaEntity entity = CourseMapper.fromCourseDtoToCourseEntity(course);
        CourseJpaEntity saved = courseDao.save(entity);
        return CourseMapper.fromCourseEntityToCourseDto(saved);
    }

    @Override
    public void delete(Long id) {
        courseDao.deleteById(id);
    }

    private List<CourseDto> mapToDtos(List<CourseJpaEntity> entities) {
        return entities.stream()
                .map(CourseMapper::fromCourseEntityToCourseDto)
                .toList();
    }
}