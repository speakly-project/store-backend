package es.speakly.store_backend.usecase;

import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.domain.repository.UserRepository;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswdUpdateUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PasswdUpdateUseCaseImpl passwdUpdateUseCaseImpl;

    private UserDto existingUser;

    @BeforeEach
    void setUp() {
        String oldPasswordPlain = "securePassword123";
        String oldPasswordHashed = BCrypt.hashpw(oldPasswordPlain, BCrypt.gensalt());

        existingUser = new UserDto(
                1L,
                "testuser",
                "example@example.es",
                "http://valid.url/profile.jpg",
                oldPasswordHashed,
                LocalDateTime.now(),
                null,
                UserRole.USER
        );
    }

    @Test
    void updatePassword_whenOk_shouldHashAndSaveNewPassword() {
        when(userRepository.findById(existingUser.id())).thenReturn(Optional.of(existingUser));

        String oldPasswordPlain = "securePassword123";
        String newPasswordPlain = "newPassword456";

        passwdUpdateUseCaseImpl.updatePassword(existingUser.id(), oldPasswordPlain, newPasswordPlain);

        ArgumentCaptor<UserDto> captor = ArgumentCaptor.forClass(UserDto.class);
        verify(userRepository).findById(existingUser.id());
        verify(userRepository).save(captor.capture());
        verifyNoMoreInteractions(userRepository);

        UserDto saved = captor.getValue();

        assertAll(
                () -> assertEquals(existingUser.id(), saved.id()),
                () -> assertNotNull(saved.password()),
                () -> assertNotEquals(newPasswordPlain, saved.password()),
                () -> assertTrue(BCrypt.checkpw(newPasswordPlain, saved.password()))
        );
    }

    @Test
    void updatePassword_whenUserDoesNotExist_shouldThrowResourceNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> passwdUpdateUseCaseImpl.updatePassword(999L, "old", "new"));
        verify(userRepository).findById(999L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updatePassword_whenOldPasswordIsWrong_shouldThrowBusinessException() {
        when(userRepository.findById(existingUser.id())).thenReturn(Optional.of(existingUser));

        assertThrows(BusinessException.class,
                () -> passwdUpdateUseCaseImpl.updatePassword(existingUser.id(), "wrongOld", "newPassword456"));

        verify(userRepository).findById(existingUser.id());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updatePassword_whenNewPasswordIsSameAsOld_shouldThrowBusinessException() {
        when(userRepository.findById(existingUser.id())).thenReturn(Optional.of(existingUser));

        String oldPasswordPlain = "securePassword123";

        assertThrows(BusinessException.class,
                () -> passwdUpdateUseCaseImpl.updatePassword(existingUser.id(), oldPasswordPlain, oldPasswordPlain));

        verify(userRepository).findById(existingUser.id());
        verifyNoMoreInteractions(userRepository);
    }
}
