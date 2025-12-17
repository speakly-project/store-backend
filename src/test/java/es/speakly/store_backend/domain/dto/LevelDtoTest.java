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

public class LevelDtoTest {
    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(" "),
                Arguments.of(""),
                Arguments.of("123456789012345678901")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void levelDto_WithInvalidData_ShouldFailValidation(String name) {
        LevelDto levelDto = new LevelDto(
                1L,
                name
        );
        assertThrows(ValidationException.class, () -> DtoValidator.validate(levelDto));
    }
}
