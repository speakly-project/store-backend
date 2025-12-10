package es.speakly.store_backend.persistence.dao;


import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;

import java.util.Optional;

public interface UserDao extends GenericDao<UserJpaEntity>{
    Optional<UserJpaEntity> findByUsername(String username);
    Optional<UserJpaEntity> findByEmail(String email);
}