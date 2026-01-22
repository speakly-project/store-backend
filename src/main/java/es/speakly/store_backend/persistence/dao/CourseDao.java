package es.speakly.store_backend.persistence.dao;

import es.speakly.store_backend.domain.dto.CourseFiltersDto;
import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;

import java.util.List;
import java.util.Optional;

public interface CourseDao {
    List<CourseJpaEntity> findAllWithFilters(int pageNumber, int pageSize, CourseFiltersDto filters);
    List<CourseJpaEntity> findAll(int pageNumber, int pageSize);
    Optional<CourseJpaEntity> findById(Long id);
    Optional<CourseJpaEntity> findByTitle(String title);
    CourseJpaEntity save(CourseJpaEntity entity);
    void deleteById(Long id);
    long count();
    long countWithFilters(CourseFiltersDto filters);
}