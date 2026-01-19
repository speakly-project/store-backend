package es.speakly.store_backend.persistence.repository;

import es.speakly.store_backend.domain.dto.LoginUserDto;
import es.speakly.store_backend.persistence.dao.AuthDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static es.speakly.store_backend.domain.model.UserRole.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthRepositoryImplTest {
    @Mock
    private AuthDao authDao;

    @InjectMocks
    private AuthRepositoryImpl authRepository;

    private LoginUserDto loginUserDto;

    @BeforeEach
    void setUp() {
        loginUserDto = new LoginUserDto(1L, "test@email.com", USER);
    }

    @Test
    void findByToken_success() {
        String token = "valid-token";

        when(authDao.findByToken(token)).thenReturn(Optional.of(loginUserDto));

        Optional<LoginUserDto> result = authRepository.findByToken(token);

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(loginUserDto.id(), result.get().id()),
                () -> assertEquals(loginUserDto.username(), result.get().username())
        );
        verify(authDao).findByToken(token);
    }

    @Test
    void findByToken_notFound() {
        String token = "invalid-token";

        when(authDao.findByToken(token)).thenReturn(Optional.empty());

        Optional<LoginUserDto> result = authRepository.findByToken(token);

        assertTrue(result.isEmpty());
        verify(authDao).findByToken(token);
    }

    @Test
    void createTokenForUser_success() {
        Long userId = 1L;
        UUID token = UUID.randomUUID();

        when(authDao.createTokenForUser(userId)).thenReturn(token);

        UUID result = authRepository.createTokenForUser(userId);

        assertNotNull(result);
        assertEquals(token, result);
        verify(authDao).createTokenForUser(userId);
    }

    @Test
    void deleteToken_success() {
        String token = "valid-token";

        doNothing().when(authDao).deleteToken(token);

        authRepository.deleteToken(token);

        verify(authDao).deleteToken(token);
    }
}
