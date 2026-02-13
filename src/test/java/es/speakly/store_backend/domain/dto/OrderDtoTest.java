package es.speakly.store_backend.domain.dto;

import es.speakly.store_backend.domain.model.OrderStatus;
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

public class OrderDtoTest {

    static Stream<Arguments> invalidValues() {
        UserDto validUser = new UserDto(1L, "user", "user@email.com", null, "pass", null, List.of(), UserRole.USER);
        return Stream.of(
                Arguments.of(null, OrderStatus.PENDING),
                Arguments.of(validUser, null)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidValues")
    void orderDto_WithInvalidData_ShouldFailValidation(UserDto user, OrderStatus status) {
        OrderDto orderDto = new OrderDto(
                1L,
                user,
                status,
                List.of(),
                new BigDecimal("10.00"),
                null,
                LocalDateTime.now()
        );
        assertThrows(ValidationException.class, () -> DtoValidator.validate(orderDto));
    }

    @Test
    void orderDto_WithNegativeTotalPrice_ShouldFailValidation() {
        UserDto validUser = new UserDto(1L, "user", "user@email.com", null, "pass", null, List.of(), UserRole.USER);
        OrderDto orderDto = new OrderDto(
                1L,
                validUser,
                OrderStatus.PENDING,
                List.of(),
                new BigDecimal("-5.00"),
                null,
                LocalDateTime.now()
        );
        assertThrows(ValidationException.class, () -> DtoValidator.validate(orderDto));
    }

    @Test
    void orderDto_WithValidData_ShouldPassValidation() {
        UserDto validUser = new UserDto(1L, "user", "user@email.com", null, "pass", null, List.of(), UserRole.USER);
        CourseDto courseDto = new CourseDto(1L, "Course", "Desc", new BigDecimal("20.00"), "Spanish", "A1", validUser, 10, LocalDateTime.now());
        OrderItemDto item = new OrderItemDto(1L, courseDto, 1L, new BigDecimal("20.00"));

        OrderDto orderDto = new OrderDto(
                1L,
                validUser,
                OrderStatus.PENDING,
                List.of(item),
                new BigDecimal("20.00"),
                null,
                LocalDateTime.now()
        );

        assertDoesNotThrow(() -> DtoValidator.validate(orderDto));
    }

    @Test
    void orderDto_WithPayedStatusAndPaidDate_ShouldPassValidation() {
        UserDto validUser = new UserDto(1L, "user", "user@email.com", null, "pass", null, List.of(), UserRole.USER);

        OrderDto orderDto = new OrderDto(
                1L,
                validUser,
                OrderStatus.PAYED,
                List.of(),
                new BigDecimal("20.00"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        assertDoesNotThrow(() -> DtoValidator.validate(orderDto));
        assertNotNull(orderDto.paidDate());
    }

    @Test
    void orderDto_RecordAccessors_ShouldReturnCorrectValues() {
        UserDto validUser = new UserDto(1L, "user", "user@email.com", null, "pass", null, List.of(), UserRole.USER);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime paidDate = now.plusHours(1);

        OrderDto orderDto = new OrderDto(
                1L,
                validUser,
                OrderStatus.PAYED,
                List.of(),
                new BigDecimal("50.00"),
                paidDate,
                now
        );

        assertAll(
                () -> assertEquals(1L, orderDto.id()),
                () -> assertEquals(validUser, orderDto.user()),
                () -> assertEquals(OrderStatus.PAYED, orderDto.status()),
                () -> assertTrue(orderDto.items().isEmpty()),
                () -> assertEquals(new BigDecimal("50.00"), orderDto.totalPrice()),
                () -> assertEquals(paidDate, orderDto.paidDate()),
                () -> assertEquals(now, orderDto.createdAt())
        );
    }
}

