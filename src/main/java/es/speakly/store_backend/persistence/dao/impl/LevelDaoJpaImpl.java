package es.speakly.store_backend.persistence.dao.impl;

import es.speakly.store_backend.persistence.dao.LevelDao;
import es.speakly.store_backend.persistence.dao.impl.entity.LevelJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class LevelDaoJpaImpl implements LevelDao {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<LevelJpaEntity> findAll(int pageNumber, int pageSize) {
        int pageIndex = Math.max(pageNumber - 1, 0);
        String sql = "SELECT l FROM LevelJpaEntity l ORDER BY l.id";
        TypedQuery<LevelJpaEntity> query = entityManager.createQuery(sql, LevelJpaEntity.class)
                .setFirstResult(pageIndex * pageSize)
                .setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public Optional<LevelJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(LevelJpaEntity.class, id));
    }

    @Override
    public Optional<LevelJpaEntity> findByName(String name) {
        try {
            String sql = "SELECT l FROM LevelJpaEntity l WHERE l.name = :name";
            LevelJpaEntity result = entityManager.createQuery(sql, LevelJpaEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(l) FROM LevelJpaEntity l", Long.class)
                .getSingleResult();
    }

    @Override
    public LevelJpaEntity save(LevelJpaEntity entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }
}

