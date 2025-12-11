package es.speakly.store_backend.persistence.repository;

import es.speakly.store_backend.domain.dto.LevelDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.LevelRepository;
import es.speakly.store_backend.persistence.dao.LevelDao;
import es.speakly.store_backend.persistence.dao.impl.entity.LevelJpaEntity;

import java.util.List;
import java.util.Optional;

public class LevelRepositoryImpl implements LevelRepository {
    private final LevelDao levelDao;

    public LevelRepositoryImpl(LevelDao levelDao) {
        this.levelDao = levelDao;
    }

    @Override
    public Page<LevelDto> findAll(int pageNumber, int pageSize) {
        List<LevelJpaEntity> entities = levelDao.findAll(pageNumber, pageSize);
        List<LevelDto> levelDtos = entities.stream()
                .map(entity -> new LevelDto(
                        entity.getId(),
                        entity.getName()
                ))
                .toList();
        long totalElements = levelDao.count();
        return new Page<>(levelDtos, pageNumber, pageSize, totalElements);
    }

    @Override
    public Optional<LevelDto> findById(Long id) {
        return levelDao.findById(id)
                .map(entity -> new LevelDto(
                        entity.getId(),
                        entity.getName()
                ));
    }

    @Override
    public Optional<LevelDto> findByName(String name) {
        return levelDao.findByName(name)
                .map(entity -> new LevelDto(
                        entity.getId(),
                        entity.getName()
                ));
    }

    @Override
    public long count() {
        return levelDao.count();
    }

    @Override
    public LevelDto save(LevelDto levelDto) {
        LevelJpaEntity entity = new LevelJpaEntity(levelDto.id(), levelDto.name());
        LevelJpaEntity savedEntity = levelDao.save(entity);
        return new LevelDto(savedEntity.getId(), savedEntity.getName());
    }
}

