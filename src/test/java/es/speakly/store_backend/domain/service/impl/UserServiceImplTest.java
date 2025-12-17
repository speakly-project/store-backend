package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.domain.repository.UserRepository;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import es.speakly.store_backend.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
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
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto1;
    private UserDto userDto2;

    @BeforeEach
    void setUp() {
        CourseDto courseDto1 = new CourseDto(
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
        userDto1 = new UserDto(
                1L,
                "testuser",
                "example@example.es",
                "http://valid.url/profile.jpg",
                "securePassword123",
                LocalDateTime.now(),
                null,
                UserRole.USER
        );
        userDto2 = new UserDto(
                2L,
                "anotheruser",
                "user2@xemple.com",
                "http://valid.url/profile2.jpg",
                "anotherSecurePassword456",
                LocalDateTime.now(),
                List.of(courseDto1),
                UserRole.ADMIN
        );
    }

    @Test
    void getAll_shouldReturnPage() {
        Page<UserDto> page = new Page<>(List.of(userDto1, userDto2), 1, 10, 2);

        when(userRepository.findAll(1, 10)).thenReturn(page);

        Page<UserDto> result = userService.getAll(1, 10);

        assertEquals(2, result.data().size());
        verify(userRepository).findAll(1, 10);
    }

    @Test
    void getAll_invalidPage_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.getAll(0, 10));
    }

    @Test
    void getById_existingUser_shouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userDto1));

        UserDto result = userService.getById(1L);

        assertEquals("testuser", result.username());
    }

    @Test
    void getById_notFound_shouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getById(1L));
    }

    @Test
    void getByEmail_existing_shouldReturnUser() {
        when(userRepository.findByEmail("example@example.es"))
                .thenReturn(Optional.of(userDto1));

        UserDto result = userService.getByEmail("example@example.es");

        assertEquals(userDto1.email(), result.email());
    }

    @Test
    void getByEmail_notFound_shouldThrowException() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getByEmail("no@mail.com"));
    }

    @Test
    void getByUsername_existing_shouldReturnUser() {
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(userDto1));

        UserDto result = userService.getByUsername("testuser");

        assertEquals("testuser", result.username());
    }

    @Test
    void getByUsername_notFound_shouldThrowException() {
        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getByUsername("nope"));
    }

    @Test
    void createUser_valid_shouldCreateAndHashPassword() {
        when(userRepository.findByUsername(userDto1.username()))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDto1.email()))
                .thenReturn(Optional.empty());
        when(userRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        UserDto result = userService.createUser(userDto1);

        assertNotEquals("securePassword123", result.password());
        assertTrue(BCrypt.checkpw("securePassword123", result.password()));
    }

    @Test
    void createUser_duplicateUsername_shouldThrowException() {
        when(userRepository.findByUsername(userDto1.username()))
                .thenReturn(Optional.of(userDto1));

        assertThrows(BusinessException.class,
                () -> userService.createUser(userDto1));
    }

    @Test
    void createUser_duplicateEmail_shouldThrowException() {
        when(userRepository.findByUsername(userDto1.username()))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDto1.email()))
                .thenReturn(Optional.of(userDto1));

        assertThrows(BusinessException.class,
                () -> userService.createUser(userDto1));
    }

    @Test
    void createUser_invalidCourseId_shouldThrowException() {
        CourseDto invalidCourse = new CourseDto(
                null, "T", "D", null, "L", "L", 1L, 10, LocalDateTime.now()
        );

        UserDto user = new UserDto(
                1L,
                "u",
                "u@mail.com",
                null,
                "pass",
                LocalDateTime.now(),
                List.of(invalidCourse),
                UserRole.USER
        );

        assertThrows(ValidationException.class,
                () -> userService.createUser(user));
    }

    @Test
    void updateUser_valid_shouldUpdate() {
        when(userRepository.findById(userDto1.id()))
                .thenReturn(Optional.of(userDto1));
        when(userRepository.findByEmail(userDto1.email()))
                .thenReturn(Optional.of(userDto1));
        when(userRepository.findByUsername(userDto1.username()))
                .thenReturn(Optional.of(userDto1));
        when(userRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        UserDto result = userService.updateUser(userDto1);

        assertEquals(userDto1.id(), result.id());
    }

    @Test
    void updateUser_notFound_shouldThrowException() {
        when(userRepository.findById(userDto1.id()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(userDto1));
    }

    @Test
    void delete_existing_shouldDelete() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userDto1));

        userService.delete(1L);

        verify(userRepository).delete(1L);
    }

    @Test
    void delete_notFound_shouldThrowException() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.delete(1L));
    }
    

}
