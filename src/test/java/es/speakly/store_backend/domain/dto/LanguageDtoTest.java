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

public class LanguageDtoTest {
    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of(null, "USER"),
                Arguments.of("null", null),
                Arguments.of(" ", "USER"),
                Arguments.of("", "USER"),
                Arguments.of("1", " USER"),
                Arguments.of("123456789012345678901234567890123456789012345678901", " USER")

        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void languageDto_WithInvalidData_ShouldFailValidation(String email, String code) {
        LanguageDto languageDto = new LanguageDto(
                1L,
                email,
                code
        );
        assertThrows(ValidationException.class, () -> DtoValidator.validate(languageDto));
    }
}
