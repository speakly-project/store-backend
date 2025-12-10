package es.speakly.store_backend.persistence.dao;

import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;

import java.util.List;
import java.util.Optional;

public interface CourseDao extends GenericDao<CourseJpaEntity> {
    Optional<CourseJpaEntity> findByTitle(String title);
    List<CourseJpaEntity> findByLanguageAndLevel(String language, String level, int pageNumber, int pageSize);
}
