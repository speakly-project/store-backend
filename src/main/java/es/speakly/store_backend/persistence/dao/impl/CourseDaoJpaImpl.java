package es.speakly.store_backend.persistence.dao.impl;

import es.speakly.store_backend.persistence.dao.CourseDao;
import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class CourseDaoJpaImpl implements CourseDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CourseJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(CourseJpaEntity.class, id));
    }

    @Override
    public Optional<CourseJpaEntity> findByTitle(String title) {
        String sql = "SELECT c FROM CourseJpaEntity c WHERE c.title = :title";
        TypedQuery<CourseJpaEntity> query = entityManager.createQuery(sql, CourseJpaEntity.class)
                .setParameter("title", title);
        List<CourseJpaEntity> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    @Override
    public List<CourseJpaEntity> findByLanguageAndLevel(String language, String level, int pageNumber, int pageSize) {
        int pageIndex = Math.max(pageNumber - 1, 0);
        String sql = "SELECT c FROM CourseJpaEntity c WHERE c.language = :language AND c.level = :level";
        TypedQuery<CourseJpaEntity> query = entityManager.createQuery(sql, CourseJpaEntity.class)
                .setParameter("language", language)
                .setParameter("level", level)
                .setFirstResult(pageIndex * pageSize)
                .setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<CourseJpaEntity> findAll(int pageNumber, int pageSize) {
        int pageIndex = Math.max(pageNumber - 1, 0);
        String sql = "SELECT c FROM CourseJpaEntity c";
        TypedQuery<CourseJpaEntity> query = entityManager.createQuery(sql, CourseJpaEntity.class)
                .setFirstResult(pageIndex * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public CourseJpaEntity insert(CourseJpaEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public CourseJpaEntity update(CourseJpaEntity entity) {
        CourseJpaEntity courseJpaEntity = entityManager.find(CourseJpaEntity.class, entity.getId());
        if (courseJpaEntity == null) {
            throw new RuntimeException("Course not found with id: " + entity.getId());
        }
        return entityManager.merge(entity);
    }

    @Override
    public void deleteById(Long id) {
        CourseJpaEntity entity = entityManager.find(CourseJpaEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(c) FROM CourseJpaEntity c", Long.class)
                .getSingleResult();
    }

}
