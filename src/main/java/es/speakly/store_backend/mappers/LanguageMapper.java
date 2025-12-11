package es.speakly.store_backend.mappers;

import es.speakly.store_backend.domain.dto.LanguageDto;
import es.speakly.store_backend.domain.model.Language;
import es.speakly.store_backend.persistence.dao.impl.entity.LanguageJpaEntity;

public class LanguageMapper {
    private static LanguageMapper INSTANCE;

    private LanguageMapper() {
    }

    public static LanguageMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LanguageMapper();
        }
        return INSTANCE;
    }

    /**
     * Converts LanguageDto to Language domain model
     * The domain model will validate the data
     */
    public static Language fromLanguageDtoToLanguage(LanguageDto languageDto) {
        if (languageDto == null) {
            return null;
        }
        return new Language(
            languageDto.id(),
            languageDto.code(),
            languageDto.name()
        );
    }

    /**
     * Converts Language domain model back to LanguageDto
     */
    public static LanguageDto fromLanguageToLanguageDto(Language language) {
        if (language == null) {
            return null;
        }
        return new LanguageDto(
            language.getId(),
            language.getName(),
            language.getCode()
        );
    }

    /**
     * Converts LanguageJpaEntity to LanguageDto
     */
    public static LanguageDto fromLanguageEntityToLanguageDto(LanguageJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new LanguageDto(
            entity.getId(),
            entity.getName(),
            entity.getCode()
        );
    }

    /**
     * Converts LanguageDto to LanguageJpaEntity
     */
    public static LanguageJpaEntity fromLanguageDtoToLanguageEntity(LanguageDto dto) {
        if (dto == null) {
            return null;
        }
        LanguageJpaEntity entity = new LanguageJpaEntity();
        entity.setId(dto.id());
        return new LanguageJpaEntity(dto.id(), dto.code(), dto.name());
    }
}

