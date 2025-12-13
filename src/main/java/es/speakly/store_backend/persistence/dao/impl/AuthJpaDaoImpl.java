package es.speakly.store_backend.persistence.dao.impl;

import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import es.speakly.store_backend.persistence.dao.AuthDao;
import es.speakly.store_backend.persistence.dao.impl.entity.SessionJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class AuthJpaDaoImpl implements AuthDao {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<UserJpaEntity> findByToken(String token) {
        try {
            String sql = "SELECT s FROM SessionJpaEntity s WHERE s.token = :token";
            UserJpaEntity user = entityManager.createQuery(sql, UserJpaEntity.class)
                    .setParameter("token", token)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public UUID createTokenForUser(Long userId) {
        UserJpaEntity userJpaEntity = entityManager.find(UserJpaEntity.class, userId);
        if (userJpaEntity == null) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        UUID uuid = UUID.randomUUID();
        LocalDateTime createAt = LocalDateTime.now();

        SessionJpaEntity session = new SessionJpaEntity(uuid.toString(), userJpaEntity, createAt);
        entityManager.persist(session);
        return uuid;
    }

    @Override
    public void deleteToken(String token) {
        String sql = "DELETE FROM SessionJpaEntity s WHERE s.token = :token";
        entityManager.createQuery(sql)
                .setParameter("token", token)
                .executeUpdate();


    }

    @Override
    public Long count() {
        return entityManager.createQuery("SELECT COUNT(s) FROM SessionJpaEntity s", Long.class)
                .getSingleResult();
    }
}
