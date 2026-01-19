package es.speakly.store_backend.domain.dto;

import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.exceptions.DtoValidator;
import es.speakly.store_backend.exceptions.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static es.speakly.store_backend.domain.model.UserRole.USER;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginUserDtoTest {
    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of(null, USER),
                Arguments.of("null", null),
                Arguments.of(" ", USER),
                Arguments.of("", USER)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void loginUserDto_WithInvalidData_ShouldFailValidation(String username, UserRole role) {
        LoginUserDto loginUserDto = new LoginUserDto(
                1L,
                username,
                role
        );
        assertThrows(ValidationException.class, () -> DtoValidator.validate(loginUserDto));
    }
}
