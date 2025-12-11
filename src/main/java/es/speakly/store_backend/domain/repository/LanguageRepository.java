package es.speakly.store_backend.domain.repository;

import es.speakly.store_backend.domain.dto.LanguageDto;
import es.speakly.store_backend.domain.model.Page;

import java.util.Optional;

public interface LanguageRepository {
    Page<LanguageDto> findAll(int pageNumber, int pageSize);
    Optional<LanguageDto> findById(Long id);
    long count();
}
