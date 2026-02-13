package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.OrderDto;
import es.speakly.store_backend.domain.dto.OrderItemDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.OrderStatus;
import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.domain.repository.OrderRepository;
import es.speakly.store_backend.domain.service.CourseService;
import es.speakly.store_backend.domain.service.UserService;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import es.speakly.store_backend.exceptions.ValidationException;
import es.speakly.store_backend.nanoServices.payment.CardPaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    @Mock
    private CardPaymentService cardPaymentService;

    @InjectMocks
    private CartServiceImpl cartService;

    private UserDto userDto;
    private CourseDto courseDto;
    private OrderItemDto orderItemDto;
    private OrderDto pendingOrderDto;
    private OrderDto processingOrderDto;
    private OrderDto payedOrderDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(
                1L,
                "testuser",
                "test@email.com",
                null,
                "hashedpassword",
                LocalDateTime.now(),
                List.of(),
                UserRole.USER
        );

        courseDto = new CourseDto(
                1L,
                "English A1",
                "Basic English",
                new BigDecimal("19.99"),
                "English",
                "A1",
                userDto,
                10,
                LocalDateTime.now()
        );

        orderItemDto = new OrderItemDto(
                1L,
                courseDto,
                1L,
                new BigDecimal("19.99")
        );

        pendingOrderDto = new OrderDto(
                1L,
                userDto,
                OrderStatus.PENDING,
                List.of(orderItemDto),
                new BigDecimal("19.99"),
                null,
                LocalDateTime.now()
        );

        processingOrderDto = new OrderDto(
                1L,
                userDto,
                OrderStatus.PROCESSING,
                List.of(orderItemDto),
                new BigDecimal("19.99"),
                null,
                LocalDateTime.now()
        );

        payedOrderDto = new OrderDto(
                1L,
                userDto,
                OrderStatus.PAYED,
                List.of(orderItemDto),
                new BigDecimal("19.99"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Nested
    class GetCartTests {

        @Test
        void getCart_existingCart_shouldReturnCart() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));

            OrderDto result = cartService.getCart(1L);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(1L, result.id()),
                    () -> assertEquals(OrderStatus.PENDING, result.status()),
                    () -> assertEquals(userDto, result.user())
            );
            verify(userService).getById(1L);
            verify(orderRepository).getActiveOrder(1L);
        }

        @Test
        void getCart_nullId_shouldThrowValidationException() {
            assertThrows(ValidationException.class, () -> cartService.getCart(null));
        }

        @Test
        void getCart_negativeId_shouldThrowValidationException() {
            assertThrows(ValidationException.class, () -> cartService.getCart(-1L));
        }

        @Test
        void getCart_zeroId_shouldThrowValidationException() {
            assertThrows(ValidationException.class, () -> cartService.getCart(0L));
        }

        @Test
        void getCart_noExistingCart_shouldCreateNewCart() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L))
                    .thenReturn(Optional.empty())
                    .thenReturn(Optional.of(pendingOrderDto));
            when(orderRepository.save(any())).thenReturn(pendingOrderDto);

            OrderDto result = cartService.getCart(1L);

            assertNotNull(result);
            verify(orderRepository, atLeast(1)).save(any());
        }
    }

    @Nested
    class CreateCartTests {

        @Test
        void createCart_validUser_shouldCreateCart() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.save(any())).thenReturn(pendingOrderDto);

            assertDoesNotThrow(() -> cartService.createCart(1L));

            verify(userService).getById(1L);
            verify(orderRepository).save(any());
        }

        @Test
        void createCart_nullId_shouldThrowValidationException() {
            assertThrows(ValidationException.class, () -> cartService.createCart(null));
        }

        @Test
        void createCart_userNotFound_shouldThrowException() {
            when(userService.getById(999L)).thenThrow(new ResourceNotFoundException("User not found"));

            assertThrows(ResourceNotFoundException.class, () -> cartService.createCart(999L));
        }
    }

    @Nested
    class DeleteCartTests {

        @Test
        void deleteCart_pendingCart_shouldClearCart() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));
            when(orderRepository.save(any())).thenReturn(pendingOrderDto);

            assertDoesNotThrow(() -> cartService.deleteCart(1L));

            verify(orderRepository).save(argThat(order ->
                    order.items().isEmpty() &&
                    order.totalPrice().compareTo(BigDecimal.ZERO) == 0
            ));
        }

        @Test
        void deleteCart_nullId_shouldThrowValidationException() {
            assertThrows(ValidationException.class, () -> cartService.deleteCart(null));
        }

        @Test
        void deleteCart_notPendingStatus_shouldThrowBusinessException() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(processingOrderDto));

            assertThrows(BusinessException.class, () -> cartService.deleteCart(1L));
        }
    }

    @Nested
    class GetCartStatusTests {

        @Test
        void getCartStatus_existingCart_shouldReturnStatus() {
            when(orderRepository.getStatus(1L)).thenReturn(Optional.of(OrderStatus.PENDING));

            OrderStatus result = cartService.getCartStatus(1L);

            assertEquals(OrderStatus.PENDING, result);
            verify(orderRepository).getStatus(1L);
        }

        @Test
        void getCartStatus_notFound_shouldThrowException() {
            when(orderRepository.getStatus(999L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> cartService.getCartStatus(999L));
        }

        @Test
        void getCartStatus_nullId_shouldThrowValidationException() {
            assertThrows(ValidationException.class, () -> cartService.getCartStatus(null));
        }
    }

    @Nested
    class UpdatePendingCartTests {

        @Test
        void updatePendingCart_validOrder_shouldUpdate() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));
            when(courseService.getById(1L)).thenReturn(courseDto);
            when(orderRepository.save(any())).thenReturn(pendingOrderDto);

            assertDoesNotThrow(() -> cartService.updatePendingCart(pendingOrderDto));

            verify(orderRepository).save(any());
        }

        @Test
        void updatePendingCart_nullOrder_shouldThrowValidationException() {
            assertThrows(ValidationException.class, () -> cartService.updatePendingCart(null));
        }

        @Test
        void updatePendingCart_notPendingStatus_shouldThrowBusinessException() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(processingOrderDto));

            assertThrows(BusinessException.class, () -> cartService.updatePendingCart(pendingOrderDto));
        }

        @Test
        void updatePendingCart_duplicateProduct_shouldThrowValidationException() {
            OrderItemDto duplicateItem1 = new OrderItemDto(1L, courseDto, 1L, new BigDecimal("19.99"));
            OrderItemDto duplicateItem2 = new OrderItemDto(2L, courseDto, 1L, new BigDecimal("19.99"));

            OrderDto orderWithDuplicates = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PENDING,
                    List.of(duplicateItem1, duplicateItem2),
                    new BigDecimal("39.98"),
                    null,
                    LocalDateTime.now()
            );

            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));

            assertThrows(ValidationException.class, () -> cartService.updatePendingCart(orderWithDuplicates));
        }
    }

    @Nested
    class UpdateCartTests {

        @Test
        void updateCart_validTransitionPendingToProcessing_shouldUpdate() {
            OrderDto orderToProcessing = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PROCESSING,
                    List.of(orderItemDto),
                    new BigDecimal("19.99"),
                    null,
                    LocalDateTime.now()
            );

            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));
            when(courseService.getById(1L)).thenReturn(courseDto);
            when(orderRepository.save(any())).thenReturn(orderToProcessing);

            assertDoesNotThrow(() -> cartService.updateCart(orderToProcessing));

            verify(orderRepository).save(any());
        }

        @Test
        void updateCart_validTransitionProcessingToPayed_shouldSetPaidDate() {
            OrderDto orderToPayed = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PAYED,
                    List.of(orderItemDto),
                    new BigDecimal("19.99"),
                    null,
                    LocalDateTime.now()
            );

            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(processingOrderDto));
            when(courseService.getById(1L)).thenReturn(courseDto);
            when(orderRepository.save(any())).thenReturn(payedOrderDto);

            assertDoesNotThrow(() -> cartService.updateCart(orderToPayed));

            verify(orderRepository).save(argThat(order ->
                    order.status() == OrderStatus.PAYED &&
                    order.paidDate() != null
            ));
        }

        @Test
        void updateCart_alreadyPayed_shouldThrowBusinessException() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(payedOrderDto));

            assertThrows(BusinessException.class, () -> cartService.updateCart(pendingOrderDto));
        }

        @Test
        void updateCart_invalidTransitionPendingToPayed_shouldThrowBusinessException() {
            OrderDto invalidTransition = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PAYED,
                    List.of(orderItemDto),
                    new BigDecimal("19.99"),
                    null,
                    LocalDateTime.now()
            );

            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));

            assertThrows(BusinessException.class, () -> cartService.updateCart(invalidTransition));
        }
    }

    @Nested
    class PayCartTests {

        @Test
        void payCart_nullUserId_shouldThrowValidationException() {
            assertThrows(ValidationException.class,
                    () -> cartService.payCart(null, "4111111111111111", "12/25", "123", "John Doe"));
        }

        @Test
        void payCart_blankCardNumber_shouldThrowValidationException() {
            assertThrows(ValidationException.class,
                    () -> cartService.payCart(1L, "", "12/25", "123", "John Doe"));
        }

        @Test
        void payCart_blankExpiryDate_shouldThrowValidationException() {
            assertThrows(ValidationException.class,
                    () -> cartService.payCart(1L, "4111111111111111", "", "123", "John Doe"));
        }

        @Test
        void payCart_blankCvv_shouldThrowValidationException() {
            assertThrows(ValidationException.class,
                    () -> cartService.payCart(1L, "4111111111111111", "12/25", "", "John Doe"));
        }

        @Test
        void payCart_blankFullName_shouldThrowValidationException() {
            assertThrows(ValidationException.class,
                    () -> cartService.payCart(1L, "4111111111111111", "12/25", "123", ""));
        }

        @Test
        void payCart_notPendingStatus_shouldThrowBusinessException() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(processingOrderDto));

            assertThrows(BusinessException.class,
                    () -> cartService.payCart(1L, "4111111111111111", "12/25", "123", "John Doe"));
        }

        @Test
        void payCart_emptyCart_shouldThrowBusinessException() {
            OrderDto emptyCart = new OrderDto(
                    1L,
                    userDto,
                    OrderStatus.PENDING,
                    new ArrayList<>(),
                    BigDecimal.ZERO,
                    null,
                    LocalDateTime.now()
            );

            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(emptyCart));

            assertThrows(BusinessException.class,
                    () -> cartService.payCart(1L, "4111111111111111", "12/25", "123", "John Doe"));
        }

        @Test
        void payCart_paymentFailed_shouldRollbackToPending() {
            when(userService.getById(1L)).thenReturn(userDto);
            when(orderRepository.getActiveOrder(1L)).thenReturn(Optional.of(pendingOrderDto));
            when(orderRepository.save(any())).thenReturn(processingOrderDto);
            doThrow(new RuntimeException("Payment declined")).when(cardPaymentService).processPayment(any(), any());

            assertThrows(BusinessException.class,
                    () -> cartService.payCart(1L, "4111111111111111", "12/25", "123", "John Doe"));

            // Verifica que se guardó el rollback a PENDING
            verify(orderRepository, atLeastOnce()).save(argThat(order ->
                    order.status() == OrderStatus.PENDING
            ));
        }
    }
}

