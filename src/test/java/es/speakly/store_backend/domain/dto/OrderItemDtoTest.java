package es.speakly.store_backend.domain.dto;

import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.exceptions.DtoValidator;
import es.speakly.store_backend.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class OrderItemDtoTest {

    private static UserDto createValidUser() {
        return new UserDto(1L, "user", "user@email.com", null, "pass", null, List.of(), UserRole.USER);
    }

    private static CourseDto createValidCourse() {
        return new CourseDto(1L, "Course", "Desc", new BigDecimal("20.00"), "Spanish", "A1", createValidUser(), 10, LocalDateTime.now());
    }

    static Stream<Arguments> invalidValues() {
        CourseDto validCourse = createValidCourse();
        return Stream.of(
                Arguments.of(validCourse, 0L, new BigDecimal("10.00")),
                Arguments.of(validCourse, -1L, new BigDecimal("10.00")),
                Arguments.of(validCourse, 1L, new BigDecimal("-5.00"))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void orderItemDto_WithInvalidData_ShouldFailValidation(CourseDto course, Long quantity, BigDecimal price) {
        OrderItemDto orderItemDto = new OrderItemDto(
                1L,
                course,
                quantity,
                price
        );
        assertThrows(ValidationException.class, () -> DtoValidator.validate(orderItemDto));
    }

    @Test
    void orderItemDto_WithValidData_ShouldPassValidation() {
        CourseDto validCourse = createValidCourse();
        OrderItemDto orderItemDto = new OrderItemDto(
                1L,
                validCourse,
                2L,
                new BigDecimal("40.00")
        );

        assertDoesNotThrow(() -> DtoValidator.validate(orderItemDto));
    }

    @Test
    void orderItemDto_WithZeroPrice_ShouldPassValidation() {
        CourseDto validCourse = createValidCourse();
        OrderItemDto orderItemDto = new OrderItemDto(
                1L,
                validCourse,
                1L,
                BigDecimal.ZERO
        );

        assertDoesNotThrow(() -> DtoValidator.validate(orderItemDto));
    }

    @Test
    void orderItemDto_RecordAccessors_ShouldReturnCorrectValues() {
        CourseDto validCourse = createValidCourse();
        BigDecimal price = new BigDecimal("25.00");

        OrderItemDto orderItemDto = new OrderItemDto(
                10L,
                validCourse,
                3L,
                price
        );

        assertAll(
                () -> assertEquals(10L, orderItemDto.id()),
                () -> assertEquals(validCourse, orderItemDto.course()),
                () -> assertEquals(3L, orderItemDto.quantity()),
                () -> assertEquals(price, orderItemDto.price())
        );
    }

    @Test
    void orderItemDto_WithNullCourse_ShouldNotFailDtoValidation() {
        OrderItemDto orderItemDto = new OrderItemDto(
                1L,
                null,
                1L,
                new BigDecimal("10.00")
        );

        assertDoesNotThrow(() -> DtoValidator.validate(orderItemDto));
    }
}

