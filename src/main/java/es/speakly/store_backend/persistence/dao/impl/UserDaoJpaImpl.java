package es.speakly.store_backend.persistence.dao.impl;

import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;
import es.speakly.store_backend.persistence.dao.UserDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class UserDaoJpaImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<UserJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UserJpaEntity.class, id));
    }

    @Override
    public Optional<UserJpaEntity> findByUsername(String username) {
        return Optional.ofNullable(entityManager.find(UserJpaEntity.class, username));
    }

    @Override
    public Optional<UserJpaEntity> findByEmail(String email) {
        return Optional.ofNullable(entityManager.find(UserJpaEntity.class, email));
    }

    @Override
    public List<UserJpaEntity> findAll(int pageNumber, int pageSize) {
        int pageIndex = Math.max(pageNumber - 1, 0);
        String sql = "SELECT u FROM UserJpaEntity u";
        TypedQuery<UserJpaEntity> query = entityManager.createQuery(sql, UserJpaEntity.class)
                .setFirstResult(pageIndex * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public UserJpaEntity insert(UserJpaEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public UserJpaEntity update(UserJpaEntity entity) {
        UserJpaEntity userJpaEntity = entityManager.find(UserJpaEntity.class, entity.getId());
        if (userJpaEntity == null){
            throw new  RuntimeException("User not found with id: " + entity.getId());
        }
        userJpaEntity.getCoursesTaken().clear();
        entityManager.merge(userJpaEntity);
        return entityManager.merge(entity);
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(entityManager.find(UserJpaEntity.class, id));
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(u) FROM UserJpaEntity u", Long.class)
                .getSingleResult();
    }
}