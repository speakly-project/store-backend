package es.speakly.store_backend.domain.service;

import es.speakly.store_backend.domain.dto.LanguageDto;
import es.speakly.store_backend.domain.model.Page;

public interface LanguageService {
    Page<LanguageDto> getAll(int pageNumber, int pageSize);
    LanguageDto getById(Long id);
    long count();
}
