package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.LevelDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.LevelRepository;
import es.speakly.store_backend.domain.service.LevelService;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;

public class LevelServiceImpl implements LevelService {
    private final LevelRepository levelRepository;

    public LevelServiceImpl(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    @Override
    public Page<LevelDto> getAll(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page number and size must be greater than 0");
        }
        return levelRepository.findAll(pageNumber, pageSize);
    }

    @Override
    public LevelDto getById(Long id) {
        return levelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Level not found with id: " + id));
    }

    @Override
    public long count() {
        return levelRepository.count();
    }

    @Override
    @Transactional
    public LevelDto createLevel(LevelDto levelDto) {
        // Validate that level name doesn't already exist
        if (levelRepository.findByName(levelDto.name()).isPresent()) {
            throw new IllegalArgumentException("Level with name '" + levelDto.name() + "' already exists");
        }
        return levelRepository.save(levelDto);
    }
}

