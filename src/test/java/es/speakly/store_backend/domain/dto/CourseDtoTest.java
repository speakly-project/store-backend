package es.speakly.store_backend.domain.dto;

import es.speakly.store_backend.exceptions.DtoValidator;
import es.speakly.store_backend.exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CourseDtoTest {
    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of(null, new BigDecimal(10), "null", "null"),
                Arguments.of("null", null, "null", "null"),
                Arguments.of("null", new BigDecimal(5), null, " null "),
                Arguments.of("null", new BigDecimal(10), " null ", null),
                Arguments.of(" ", new BigDecimal(10), " blank", " blank"),
                Arguments.of("null", new BigDecimal(0), "null", "null"),
                Arguments.of("null", new BigDecimal(-5), "null", "null"),
                Arguments.of("null", new BigDecimal(10), " ", " blank"),
                Arguments.of("null", new BigDecimal(10), "blank ", " ")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void courseDto_WithInvalidData_ShouldFailValidation(String title, BigDecimal price, String language, String level) {
        CourseDto courseDto = new CourseDto(
                1L,
                title,
                "Valid description",
                price,
                language,
                level,
                1L,
                10,
                LocalDateTime.now()
        );
        assertThrows(ValidationException.class, () -> DtoValidator.validate(courseDto));
    }
}

