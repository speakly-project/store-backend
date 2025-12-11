package es.speakly.store_backend.persistence.repository;

import es.speakly.store_backend.domain.dto.LanguageDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.LanguageRepository;
import es.speakly.store_backend.persistence.dao.LanguageDao;
import es.speakly.store_backend.persistence.dao.impl.entity.LanguageJpaEntity;

import java.util.List;
import java.util.Optional;

public class LanguageRepositoryImpl implements LanguageRepository {
    private final LanguageDao languageDao;

    public LanguageRepositoryImpl(LanguageDao languageDao) {
        this.languageDao = languageDao;
    }

    @Override
    public Page<LanguageDto> findAll(int pageNumber, int pageSize) {
        List<LanguageJpaEntity> entities = languageDao.findAll(pageNumber, pageSize);
        List<LanguageDto> languageDtos = entities.stream()
                .map(entity -> new LanguageDto(
                        entity.getId(),
                        entity.getName(),
                        entity.getCode()
                ))
                .toList();
        long totalElements = languageDao.count();
        return new Page<>(languageDtos, pageNumber, pageSize, totalElements);
    }

    @Override
    public Optional<LanguageDto> findById(Long id) {
        return languageDao.findById(id)
                .map(entity -> new LanguageDto(
                        entity.getId(),
                        entity.getName(),
                        entity.getCode()
                ));
    }

    @Override
    public long count() {
        return languageDao.count();
    }
}
