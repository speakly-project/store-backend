package es.speakly.store_backend.domain.service.impl;


import es.speakly.store_backend.domain.dto.LoginUserDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.domain.repository.AuthRepository;
import es.speakly.store_backend.domain.repository.UserRepository;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static es.speakly.store_backend.domain.model.UserRole.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private AuthRepository authRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginUserDto loginUserDto;
    private UserDto userDto;

    @BeforeEach
     void setUp() {
        String rawPassword = "password123";
        String encryptedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        userDto = new UserDto(
                1L,
                "testuser",
                "test@email.com",
                null,
                encryptedPassword,
                LocalDateTime.now(),
                List.of(),
                USER
        );

        loginUserDto = new LoginUserDto(1L, "test@email.com", USER);
    }


    @Nested
    class GetUserFromTokenTests {
        @Test
        void getUserFromToken_success() {
            String token = "valid-token";

            when(authRepository.findByToken(token)).thenReturn(Optional.of(loginUserDto));

            LoginUserDto result = authService.getUserFromToken(token);

            assertNotNull(result);
            assertEquals(loginUserDto.id(), result.id());
            assertEquals(loginUserDto.username(), result.username());
            verify(authRepository).findByToken(token);
        }

        @Test
        void getUserFromToken_invalidToken_throwsException() {
            String token = "invalid-token";

            when(authRepository.findByToken(token)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> authService.getUserFromToken(token));

            verify(authRepository).findByToken(token);
        }
    }

    @Nested
    class LoginTests {

        @Test
        void createTokenForUser_success() {
            String rawPassword = "password123";
            UserDto loginRequest = new UserDto(
                    null,
                    null,
                    "test@email.com",
                    null,
                    rawPassword,
                    null,
                    null,
                    USER
            );

            UUID token = UUID.randomUUID();

            when(userRepository.findByEmail(loginRequest.email()))
                    .thenReturn(Optional.of(userDto));
            when(authRepository.createTokenForUser(userDto.id()))
                    .thenReturn(token);

            String result = authService.createTokenForUser(loginRequest);

            assertNotNull(result);
            assertEquals(token.toString(), result);
            verify(userRepository).findByEmail(loginRequest.email());
            verify(authRepository).createTokenForUser(userDto.id());
        }

        @Test
        void createTokenForUser_userNotFound_throwsException() {
            when(userRepository.findByEmail(userDto.email()))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> authService.createTokenForUser(userDto));

            verify(userRepository).findByEmail(userDto.email());
            verifyNoInteractions(authRepository);
        }

        @Test
        void createTokenForUser_invalidPassword_throwsException() {
            UserDto loginRequest = new UserDto(
                    null,
                    null,
                    "test@email.com",
                    null,
                    "wrongPassword",
                    null,
                    null,
                    USER
            );

            when(userRepository.findByEmail(loginRequest.email()))
                    .thenReturn(Optional.of(userDto));

            assertThrows(BusinessException.class,
                    () -> authService.createTokenForUser(loginRequest));

            verify(userRepository).findByEmail(loginRequest.email());
            verifyNoInteractions(authRepository);
        }
    }

    @Nested
    class DeleteTokenTests {
        @Test
        void deleteToken_success() {
            String token = "valid-token";

            authService.deleteToken(token);

            verify(authRepository).deleteToken(token);
        }

        @Test
        void deleteToken_nullToken_throwsException() {
            assertThrows(BusinessException.class,
                    () -> authService.deleteToken(null));

            verifyNoInteractions(authRepository);
        }

        @Test
        void deleteToken_blankToken_throwsException() {
            assertThrows(BusinessException.class,
                    () -> authService.deleteToken("  "));

            verifyNoInteractions(authRepository);
        }
    }
}




