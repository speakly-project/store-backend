package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.LanguageDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.LanguageRepository;
import es.speakly.store_backend.exceptions.DtoValidator;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LanguageServiceImplTest {

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LanguageServiceImpl languageService;

    @Test
    void getAll_whenPageNumberLessThan1_shouldThrowIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> languageService.getAll(0, 10)
        );
        assertEquals("Page number and size must be greater than 0", ex.getMessage());
        verifyNoInteractions(languageRepository);
    }

    @Test
    void getAll_whenPageSizeLessThan1_shouldThrowIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> languageService.getAll(1, 0)
        );
        assertEquals("Page number and size must be greater than 0", ex.getMessage());
        verifyNoInteractions(languageRepository);
    }

    @Test
    void getAll_shouldReturnPageWithValidatedLanguages() {
        LanguageDto dirty1 = new LanguageDto(1L, "English", " en ");
        LanguageDto dirty2 = new LanguageDto(2L, "Spanish", "es");

        Page<LanguageDto> repoPage = new Page<>(
                List.of(dirty1, dirty2),
                1,
                10,
                2L
        );

        when(languageRepository.findAll(1, 10)).thenReturn(repoPage);

        Page<LanguageDto> result = languageService.getAll(1, 10);

        assertAll(
                () -> assertNotNull(result),
                () -> assertDoesNotThrow(() -> DtoValidator.validate(result.data().get(0))),
                () -> assertDoesNotThrow(() -> DtoValidator.validate(result.data().get(1))),
                () -> assertEquals(1, result.pageNumber()),
                () -> assertEquals(10, result.pageSize()),
                () -> assertEquals(2L, result.totalElements()),
                () -> assertNotNull(result.data()),
                () -> assertEquals(2, result.data().size()),
                () -> assertEquals(1L, result.data().get(0).id()),
                () -> assertEquals("English", result.data().get(0).name()),
                () -> assertEquals("en", result.data().get(0).code()),
                () -> assertEquals(2L, result.data().get(1).id()),
                () -> assertEquals("Spanish", result.data().get(1).name()),
                () -> assertEquals("es", result.data().get(1).code())
        );

        verify(languageRepository).findAll(1, 10);
        verifyNoMoreInteractions(languageRepository);
    }

    @Test
    void getById_whenFound_shouldReturnValidatedDto() {
        LanguageDto dirty = new LanguageDto(10L, "French", " fr ");
        when(languageRepository.findById(10L)).thenReturn(Optional.of(dirty));

        LanguageDto result = languageService.getById(10L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertDoesNotThrow(() -> DtoValidator.validate(result)),
                () -> assertEquals(10L, result.id()),
                () -> assertEquals("French", result.name()),
                () -> assertEquals("fr", result.code())
        );

        verify(languageRepository).findById(10L);
        verifyNoMoreInteractions(languageRepository);
    }

    @Test
    void getById_whenNotFound_shouldThrowResourceNotFoundException() {
        when(languageRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> languageService.getById(999L)
        );

        assertEquals("Language not found with id: 999", ex.getMessage());
        verify(languageRepository).findById(999L);
        verifyNoMoreInteractions(languageRepository);
    }

    @Test
    void count_shouldDelegateToRepository() {
        when(languageRepository.count()).thenReturn(12L);

        long result = languageService.count();

        assertEquals(12L, result);
        verify(languageRepository).count();
        verifyNoMoreInteractions(languageRepository);
    }
}
