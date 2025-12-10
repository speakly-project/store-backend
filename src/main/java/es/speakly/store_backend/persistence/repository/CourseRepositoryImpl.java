package es.speakly.store_backend.persistence.repository;

import es.speakly.store_backend.domain.dto.CourseDto;
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
    public Page<CourseDto> findAll(int pageNumber, int pageSize) {
        List<CourseJpaEntity> entities = courseDao.findAll(pageNumber, pageSize);
        List<CourseDto> courseDtos = entities.stream()
                .map(CourseMapper::fromCourseEntityToCourseDto)
                .toList();
        long totalElements = courseDao.count();
        return new Page<>(courseDtos, pageNumber, pageSize, totalElements);
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
    public Page<CourseDto> findByLanguageAndLevel(String language, String level, int pageNumber, int pageSize) {
        List<CourseJpaEntity> entities = courseDao.findByLanguageAndLevel(language, level, pageNumber, pageSize);
        List<CourseDto> courseDtos = entities.stream()
                .map(CourseMapper::fromCourseEntityToCourseDto)
                .toList();
        long totalElements = entities.size();
        return new Page<>(courseDtos, pageNumber, pageSize, totalElements);
    }

    @Override
    public CourseDto save(CourseDto course) {
        CourseJpaEntity entity = CourseMapper.fromCourseDtoToCourseEntity(course);
        if (course.id() == null) {
            return CourseMapper.fromCourseEntityToCourseDto(courseDao.insert(entity));
        }
        return CourseMapper.fromCourseEntityToCourseDto(courseDao.update(entity));

    }

    @Override
    public void delete(Long id) {
        courseDao.deleteById(id);
    }
}
