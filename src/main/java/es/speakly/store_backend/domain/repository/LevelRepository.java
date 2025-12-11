package es.speakly.store_backend.domain.repository;

import es.speakly.store_backend.domain.dto.LevelDto;
import es.speakly.store_backend.domain.model.Page;

import java.util.Optional;

public interface LevelRepository {
    Page<LevelDto> findAll(int pageNumber, int pageSize);
    Optional<LevelDto> findById(Long id);
    Optional<LevelDto> findByName(String name);
    long count();
    LevelDto save(LevelDto levelDto);
}

