package es.speakly.store_backend.domain.dto;

import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.exceptions.DtoValidator;
import es.speakly.store_backend.exceptions.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static es.speakly.store_backend.domain.model.UserRole.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDtoTest {
    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of(null, "new BigDecimal(10)", "null", USER),
                Arguments.of("null", null, "null", USER),
                Arguments.of("null"," new BigDecimal(5)", null, USER),
                Arguments.of("null", "new BigDecimal(10)", " null ", null),
                Arguments.of(" ", "new BigDecimal(10)", " blank", USER),
                Arguments.of("null", " ", "null", USER),
                Arguments.of("null", "new BigDecimal(-5)", " ", USER)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void userDto_WithInvalidData_ShouldFailValidation(String username, String email, String password, UserRole role) {
        UserDto userDto = new UserDto(
                1L,
                username,
                email,
                "http://valid.url/profile.jpg",
                password,
                null,
                null,
                role
        );
        assertThrows(ValidationException.class, () -> DtoValidator.validate(userDto));
    }
}
