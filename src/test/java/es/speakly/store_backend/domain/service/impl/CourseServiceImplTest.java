package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.CourseRepository;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private CourseDto courseDto1;
    private CourseDto courseDto2;

    @BeforeEach
    void setUp() {
        courseDto1 = new CourseDto(
                1L,
                "Spanish for Beginners",
                "An introductory course to Spanish.",
                null,
                "Spanish",
                "Beginner",
                1L,
                10,
                LocalDateTime.now()
        );

        courseDto2 = new CourseDto(
                2L,
                "French Intermediate",
                "Intermediate French course.",
                null,
                "French",
                "Intermediate",
                2L,
                15,
                LocalDateTime.now()
        );
    }

    @Test
    void getAll_shouldReturnPage() {
        Page<CourseDto> page = new Page<>(List.of(courseDto1, courseDto2), 1, 10, 2);
        when(courseRepository.findAll(1, 10)).thenReturn(page);

        Page<CourseDto> result = courseService.getAll(1, 10);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.data()),
                () -> assertEquals(2, result.data().size()),
                () -> assertEquals(1, result.pageNumber()),
                () -> assertEquals(10, result.pageSize()),
                () -> assertEquals(2, result.totalElements()),
                () -> assertEquals(courseDto1.id(), result.data().getFirst().id()),
                () -> assertEquals(courseDto2.id(), result.data().get(1).id())
        );
        verify(courseRepository).findAll(1, 10);
    }

    @Test
    void getAll_invalidPage_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> courseService.getAll(0, 10)),
                () -> assertThrows(IllegalArgumentException.class, () -> courseService.getAll(1, 0))
        );
    }

    @Test
    void getById_existingCourse_shouldReturnCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseDto1));

        CourseDto result = courseService.getById(1L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(courseDto1.id(), result.id()),
                () -> assertEquals(courseDto1.title(), result.title()),
                () -> assertEquals(courseDto1.description(), result.description())
        );
        verify(courseRepository).findById(1L);
    }

    @Test
    void getById_notFound_shouldThrowException() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrows(ResourceNotFoundException.class, () -> courseService.getById(999L))
        );
        verify(courseRepository).findById(999L);
    }

    @Test
    void getByTitle_existing_shouldReturnCourse() {
        when(courseRepository.findByTitle(courseDto1.title())).thenReturn(Optional.of(courseDto1));

        CourseDto result = courseService.getByTitle(courseDto1.title());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(courseDto1.id(), result.id()),
                () -> assertEquals(courseDto1.title(), result.title())
        );
        verify(courseRepository).findByTitle(courseDto1.title());
    }

    @Test
    void getByTitle_notFound_shouldThrowException() {
        when(courseRepository.findByTitle(any())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrows(ResourceNotFoundException.class, () -> courseService.getByTitle("nope"))
        );
        verify(courseRepository).findByTitle("nope");
    }

    @Test
    void getByLanguageAndLevel_shouldReturnPage() {
        Page<CourseDto> page = new Page<>(List.of(courseDto1), 1, 10, 1);
        when(courseRepository.findByLanguageAndLevel("Spanish", "Beginner", 1, 10)).thenReturn(page);

        Page<CourseDto> result = courseService.getByLanguageAndLevel("Spanish", "Beginner", 1, 10);

        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.data()),
                () -> assertEquals(1, result.data().size()),
                () -> assertEquals(courseDto1.id(), result.data().getFirst().id())
        );
        verify(courseRepository).findByLanguageAndLevel("Spanish", "Beginner", 1, 10);
    }

    @Test
    void getByLanguageAndLevel_invalidPage_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> courseService.getByLanguageAndLevel("Spanish", "Beginner", 0, 10)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> courseService.getByLanguageAndLevel("Spanish", "Beginner", 1, 0))
        );
    }

    @Test
    void createCourse_valid_shouldCreate() {
        when(courseRepository.findByTitle(courseDto1.title())).thenReturn(Optional.empty());
        when(courseRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CourseDto result = courseService.createCourse(courseDto1);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(courseDto1.title(), result.title()),
                () -> assertEquals(courseDto1.language(), result.language()),
                () -> assertEquals(courseDto1.level(), result.level())
        );
        verify(courseRepository).findByTitle(courseDto1.title());
        verify(courseRepository).save(any());
    }

    @Test
    void createCourse_duplicateTitle_shouldThrowException() {
        when(courseRepository.findByTitle(courseDto1.title())).thenReturn(Optional.of(courseDto1));

        assertAll(
                () -> assertThrows(BusinessException.class, () -> courseService.createCourse(courseDto1))
        );
        verify(courseRepository).findByTitle(courseDto1.title());
    }

    @Test
    void updateCourse_valid_shouldUpdate() {
        when(courseRepository.findById(courseDto1.id())).thenReturn(Optional.of(courseDto1));
        when(courseRepository.findByTitle(courseDto1.title())).thenReturn(Optional.of(courseDto1));
        when(courseRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CourseDto result = courseService.updateCourse(courseDto1);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(courseDto1.id(), result.id()),
                () -> assertEquals(courseDto1.title(), result.title())
        );
        verify(courseRepository).findById(courseDto1.id());
        verify(courseRepository).findByTitle(courseDto1.title());
        verify(courseRepository).save(any());
    }

    @Test
    void updateCourse_notFound_shouldThrowException() {
        when(courseRepository.findById(courseDto1.id())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrows(ResourceNotFoundException.class, () -> courseService.updateCourse(courseDto1))
        );
        verify(courseRepository).findById(courseDto1.id());
    }

    @Test
    void updateCourse_duplicateTitleFromOtherCourse_shouldThrowException() {
        CourseDto other = new CourseDto(
                99L,
                courseDto1.title(),
                "Other",
                null,
                courseDto1.language(),
                courseDto1.level(),
                courseDto1.teacherId(),
                courseDto1.duration(),
                LocalDateTime.now()
        );

        when(courseRepository.findById(courseDto1.id())).thenReturn(Optional.of(courseDto1));
        when(courseRepository.findByTitle(courseDto1.title())).thenReturn(Optional.of(other));

        assertAll(
                () -> assertThrows(BusinessException.class, () -> courseService.updateCourse(courseDto1))
        );
        verify(courseRepository).findById(courseDto1.id());
        verify(courseRepository).findByTitle(courseDto1.title());
    }

    @Test
    void delete_existing_shouldDelete() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseDto1));

        assertAll(
                () -> assertDoesNotThrow(() -> courseService.delete(1L))
        );

        verify(courseRepository).findById(1L);
        verify(courseRepository).delete(1L);
    }

    @Test
    void delete_notFound_shouldThrowException() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrows(ResourceNotFoundException.class, () -> courseService.delete(999L))
        );
        verify(courseRepository).findById(999L);
    }
}

