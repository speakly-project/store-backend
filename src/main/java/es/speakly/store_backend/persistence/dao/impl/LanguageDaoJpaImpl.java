package es.speakly.store_backend.persistence.dao.impl;

import es.speakly.store_backend.persistence.dao.LanguageDao;
import es.speakly.store_backend.persistence.dao.impl.entity.LanguageJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class LanguageDaoJpaImpl implements LanguageDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<LanguageJpaEntity> findAll(int pageNumber, int pageSize) {
        int pageIndex = Math.max(pageNumber - 1, 0);
        String sql = "SELECT l FROM LanguageJpaEntity l";
        TypedQuery<LanguageJpaEntity> query = entityManager.createQuery(sql, LanguageJpaEntity.class)
                .setFirstResult(pageIndex * pageSize)
                .setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public Optional<LanguageJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(LanguageJpaEntity.class, id));
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(l) FROM LanguageJpaEntity l", Long.class)
                .getSingleResult();
    }
}
