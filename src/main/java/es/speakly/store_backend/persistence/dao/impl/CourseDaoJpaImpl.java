package es.speakly.store_backend.persistence.dao.impl;

import es.speakly.store_backend.domain.dto.CourseFiltersDto;
import es.speakly.store_backend.persistence.dao.CourseDao;
import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDaoJpaImpl implements CourseDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CourseJpaEntity> findAllWithFilters(int pageNumber, int pageSize, CourseFiltersDto filters) {
        StringBuilder jpql = new StringBuilder("SELECT c FROM CourseJpaEntity c WHERE 1=1");
        boolean hasLanguage = filters != null && filters.language() != null && !filters.language().isBlank();
        boolean hasLevel = filters != null && filters.level() != null && !filters.level().isBlank();
        boolean hasMinPrice = filters != null && filters.minPrice() != null;
        boolean hasMaxPrice = filters != null && filters.maxPrice() != null;
        boolean hasSort = filters != null && filters.sortBy() != null && !filters.sortBy().isBlank();

        if (hasLanguage) jpql.append(" AND c.language = :language");
        if (hasLevel) jpql.append(" AND c.level = :level");
        if (hasMinPrice) jpql.append(" AND c.price >= :minPrice");
        if (hasMaxPrice) jpql.append(" AND c.price <= :maxPrice");

        // orden: ascPrice/descPrice, si no, sin ORDER BY
        if (hasSort) {
            String sort = filters.sortBy();
            if ("ascPrice".equalsIgnoreCase(sort)) {
                jpql.append(" ORDER BY c.price ASC");
            } else if ("descPrice".equalsIgnoreCase(sort)) {
                jpql.append(" ORDER BY c.price DESC");
            }
        }

        TypedQuery<CourseJpaEntity> query = entityManager.createQuery(jpql.toString(), CourseJpaEntity.class);

        if (hasLanguage) query.setParameter("language", filters.language());
        if (hasLevel) query.setParameter("level", filters.level());
        if (hasMinPrice) query.setParameter("minPrice", new BigDecimal(filters.minPrice()));
        if (hasMaxPrice) query.setParameter("maxPrice", new BigDecimal(filters.maxPrice()));

        int pageIndex = Math.max(pageNumber - 1, 0);
        return query.setFirstResult(pageIndex * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public List<CourseJpaEntity> findAll(int pageNumber, int pageSize) {
        // Reutiliza findAllWithFilters sin filtros
        return findAllWithFilters(pageNumber, pageSize, null);
    }

    @Override
    public Optional<CourseJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(CourseJpaEntity.class, id));
    }

    @Override
    public Optional<CourseJpaEntity> findByTitle(String title) {
        TypedQuery<CourseJpaEntity> query = entityManager.createQuery(
                        "SELECT c FROM CourseJpaEntity c WHERE c.title = ?1",
                        CourseJpaEntity.class)
                .setParameter(1, title);

        List<CourseJpaEntity> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public CourseJpaEntity save(CourseJpaEntity entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
            return entity;
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

    @Override
    public long countWithFilters(CourseFiltersDto filters) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(c) FROM CourseJpaEntity c WHERE 1=1");

        boolean hasMinPrice = filters != null && filters.minPrice() != null;
        boolean hasMaxPrice = filters != null && filters.maxPrice() != null;
        boolean hasLanguage = filters != null && filters.language() != null && !filters.language().isBlank();
        boolean hasLevel = filters != null && filters.level() != null && !filters.level().isBlank();

        if (hasMinPrice) jpql.append(" AND c.price >= :minPrice");
        if (hasMaxPrice) jpql.append(" AND c.price <= :maxPrice");
        if (hasLanguage) jpql.append(" AND c.language = :language");
        if (hasLevel) jpql.append(" AND c.level = :level");

        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);

        if (hasMinPrice) query.setParameter("minPrice", new BigDecimal(filters.minPrice()));
        if (hasMaxPrice) query.setParameter("maxPrice", new BigDecimal(filters.maxPrice()));
        if (hasLanguage) query.setParameter("language", filters.language());
        if (hasLevel) query.setParameter("level", filters.level());

        return query.getSingleResult();
    }

}