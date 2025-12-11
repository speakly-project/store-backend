package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.LanguageDto;
import es.speakly.store_backend.domain.model.Language;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.LanguageRepository;
import es.speakly.store_backend.domain.service.LanguageService;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import es.speakly.store_backend.mappers.LanguageMapper;

import java.util.List;

public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public Page<LanguageDto> getAll(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page number and size must be greater than 0");
        }

        Page<LanguageDto> languageDtoPage = languageRepository.findAll(pageNumber, pageSize);

        // Convert to domain model to apply business logic, then back to DTO
        List<LanguageDto> validatedLanguages = languageDtoPage.data()
                .stream()
                .map(LanguageMapper::fromLanguageDtoToLanguage)  // DTO → Domain Model (validates)
                .map(LanguageMapper::fromLanguageToLanguageDto)   // Domain Model → DTO
                .toList();

        return new Page<>(
                validatedLanguages,
                languageDtoPage.pageNumber(),
                languageDtoPage.pageSize(),
                languageDtoPage.totalElements()
        );
    }

    @Override
    public LanguageDto getById(Long id) {
        return languageRepository.findById(id)
                .map(LanguageMapper::fromLanguageDtoToLanguage)  // Validate through domain model
                .map(LanguageMapper::fromLanguageToLanguageDto)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + id));
    }

    @Override
    public long count() {
        return languageRepository.count();
    }
}
