package es.speakly.store_backend.persistence.dao;

import es.speakly.store_backend.persistence.dao.impl.entity.LevelJpaEntity;

import java.util.List;
import java.util.Optional;

public interface LevelDao {
    List<LevelJpaEntity> findAll(int pageNumber, int pageSize);
    Optional<LevelJpaEntity> findById(Long id);
    Optional<LevelJpaEntity> findByName(String name);
    long count();
    LevelJpaEntity save(LevelJpaEntity entity);
}

