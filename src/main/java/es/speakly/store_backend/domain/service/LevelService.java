package es.speakly.store_backend.domain.service;

import es.speakly.store_backend.domain.dto.LevelDto;
import es.speakly.store_backend.domain.model.Page;

public interface LevelService {
    Page<LevelDto> getAll(int pageNumber, int pageSize);
    LevelDto getById(Long id);
    long count();
    LevelDto createLevel(LevelDto levelDto);
}

