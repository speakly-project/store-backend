package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.LevelDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.LevelRepository;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LevelServiceImplTest {

    @Mock
    private LevelRepository levelRepository;

    @InjectMocks
    private LevelServiceImpl levelService;

    private LevelDto levelDto1;
    private LevelDto levelDto2;

    @BeforeEach
    void setUp() {
        levelDto1 = new LevelDto(1L, "A1");
        levelDto2 = new LevelDto(2L, "B1");
    }

    @Test
    void getAll_shouldReturnPage() {
        Page<LevelDto> page = new Page<>(List.of(levelDto1, levelDto2), 1, 10, 2);
        when(levelRepository.findAll(1, 10)).thenReturn(page);

        Page<LevelDto> result = levelService.getAll(1, 10);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.data()),
                () -> assertEquals(2, result.data().size()),
                () -> assertEquals(1, result.pageNumber()),
                () -> assertEquals(10, result.pageSize()),
                () -> assertEquals(2, result.totalElements()),
                () -> assertEquals(levelDto1.id(), result.data().get(0).id()),
                () -> assertEquals(levelDto2.id(), result.data().get(1).id())
        );
        verify(levelRepository).findAll(1, 10);
    }

    @Test
    void getAll_invalidPage_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> levelService.getAll(0, 10)),
                () -> assertThrows(IllegalArgumentException.class, () -> levelService.getAll(1, 0))
        );
    }

    @Test
    void getById_existingLevel_shouldReturnLevel() {
        when(levelRepository.findById(1L)).thenReturn(Optional.of(levelDto1));

        LevelDto result = levelService.getById(1L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(levelDto1.id(), result.id()),
                () -> assertEquals(levelDto1.name(), result.name())
        );
        verify(levelRepository).findById(1L);
    }

    @Test
    void getById_notFound_shouldThrowException() {
        when(levelRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> levelService.getById(999L));
        verify(levelRepository).findById(999L);
    }

    @Test
    void count_shouldReturnRepositoryCount() {
        when(levelRepository.count()).thenReturn(2L);

        long result = levelService.count();

        assertEquals(2L, result);
        verify(levelRepository).count();
    }

    @Test
    void createLevel_valid_shouldSave() {
        when(levelRepository.findByName(levelDto1.name())).thenReturn(Optional.empty());
        when(levelRepository.save(any(LevelDto.class))).thenAnswer(inv -> inv.getArgument(0));

        LevelDto result = levelService.createLevel(levelDto1);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(levelDto1.id(), result.id()),
                () -> assertEquals(levelDto1.name(), result.name())
        );
        verify(levelRepository).findByName(levelDto1.name());
        verify(levelRepository).save(any(LevelDto.class));
    }

    @Test
    void createLevel_duplicateName_shouldThrowException() {
        when(levelRepository.findByName(levelDto1.name())).thenReturn(Optional.of(levelDto1));

        assertThrows(IllegalArgumentException.class, () -> levelService.createLevel(levelDto1));
        verify(levelRepository).findByName(levelDto1.name());
    }
}