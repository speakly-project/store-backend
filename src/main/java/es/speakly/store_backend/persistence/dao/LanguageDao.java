package es.speakly.store_backend.persistence.dao;

import es.speakly.store_backend.persistence.dao.impl.entity.LanguageJpaEntity;

import java.util.List;
import java.util.Optional;

public interface LanguageDao {
    List<LanguageJpaEntity> findAll(int pageNumber, int pageSize);
    Optional<LanguageJpaEntity> findById(Long id);
    long count();
}
