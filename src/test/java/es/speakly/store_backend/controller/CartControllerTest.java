package es.speakly.store_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.speakly.store_backend.annotations.AuthenticationInterceptor;
import es.speakly.store_backend.controller.webmodel.request.CartPaymentRequest;
import es.speakly.store_backend.controller.webmodel.request.OrderUpdateRequest;
import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.OrderDto;
import es.speakly.store_backend.domain.dto.OrderItemDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.OrderStatus;
import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.domain.service.CartService;
import es.speakly.store_backend.domain.service.UserService;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationInterceptor authenticationInterceptor;

    private UserDto userDto;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() throws Exception {
        when(authenticationInterceptor.preHandle(
                any(),
                any(),
                any()
        )).thenReturn(true);

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

        CourseDto courseDto = new CourseDto(
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

        OrderItemDto orderItemDto = new OrderItemDto(
                1L,
                courseDto,
                1L,
                new BigDecimal("19.99")
        );

        orderDto = new OrderDto(
                1L,
                userDto,
                OrderStatus.PENDING,
                List.of(orderItemDto),
                new BigDecimal("19.99"),
                null,
                LocalDateTime.now()
        );
    }

    @Nested
    class GetCartTests {

        @Test
        void getCart_existingUser_shouldReturnCart() throws Exception {
            when(cartService.getCart(1L)).thenReturn(orderDto);

            mockMvc.perform(get("/api/speakly/cart/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.userId").value(1))
                    .andExpect(jsonPath("$.orderStatus").value("PENDING"))
                    .andExpect(jsonPath("$.orderItems").isArray())
                    .andExpect(jsonPath("$.orderItems.length()").value(1))
                    .andExpect(jsonPath("$.totalPrice").value(19.99));

            verify(cartService).getCart(1L);
        }

        @Test
        void getCart_notFound_shouldReturn404() throws Exception {
            when(cartService.getCart(999L)).thenThrow(new ResourceNotFoundException("User not found"));

            mockMvc.perform(get("/api/speakly/cart/999"))
                    .andExpect(status().isNotFound());

            verify(cartService).getCart(999L);
        }
    }

    @Nested
    class UpdateCartTests {

        @Test
        void updateCart_validRequest_shouldReturn200() throws Exception {
            OrderUpdateRequest updateRequest = new OrderUpdateRequest(
                    1L,
                    1L,
                    new Long[]{1L},
                    "PENDING"
            );

            when(userService.getById(1L)).thenReturn(userDto);
            doNothing().when(cartService).updatePendingCart(any(OrderDto.class));

            mockMvc.perform(put("/api/speakly/cart")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk());

            verify(userService).getById(1L);
            verify(cartService).updatePendingCart(any(OrderDto.class));
        }

        @Test
        void updateCart_userNotFound_shouldReturn404() throws Exception {
            OrderUpdateRequest updateRequest = new OrderUpdateRequest(
                    1L,
                    999L,
                    new Long[]{1L},
                    "PENDING"
            );

            when(userService.getById(999L)).thenThrow(new ResourceNotFoundException("User not found"));

            mockMvc.perform(put("/api/speakly/cart")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isNotFound());

            verify(userService).getById(999L);
        }
    }

    @Nested
    class PayCartTests {

        @Test
        void payCart_validRequest_shouldReturn200() throws Exception {
            CartPaymentRequest paymentRequest = new CartPaymentRequest(
                    1L,
                    "4111111111111111",
                    "12/25",
                    "123",
                    "John Doe"
            );

            doNothing().when(cartService).payCart(
                    eq(1L),
                    eq("4111111111111111"),
                    eq("12/25"),
                    eq("123"),
                    eq("John Doe")
            );

            mockMvc.perform(post("/api/speakly/cart/pay")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(paymentRequest)))
                    .andExpect(status().isOk());

            verify(cartService).payCart(1L, "4111111111111111", "12/25", "123", "John Doe");
        }

        @Test
        void payCart_emptyCart_shouldReturn500() throws Exception {
            CartPaymentRequest paymentRequest = new CartPaymentRequest(
                    1L,
                    "4111111111111111",
                    "12/25",
                    "123",
                    "John Doe"
            );

            doThrow(new BusinessException("Cannot pay for an empty cart"))
                    .when(cartService).payCart(
                            eq(1L),
                            eq("4111111111111111"),
                            eq("12/25"),
                            eq("123"),
                            eq("John Doe")
                    );

            mockMvc.perform(post("/api/speakly/cart/pay")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(paymentRequest)))
                    .andExpect(status().isInternalServerError());

            verify(cartService).payCart(1L, "4111111111111111", "12/25", "123", "John Doe");
        }

        @Test
        void payCart_paymentFailed_shouldReturn500() throws Exception {
            CartPaymentRequest paymentRequest = new CartPaymentRequest(
                    1L,
                    "4111111111111111",
                    "12/25",
                    "123",
                    "John Doe"
            );

            doThrow(new BusinessException("Payment failed: Insufficient funds"))
                    .when(cartService).payCart(
                            eq(1L),
                            eq("4111111111111111"),
                            eq("12/25"),
                            eq("123"),
                            eq("John Doe")
                    );

            mockMvc.perform(post("/api/speakly/cart/pay")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(paymentRequest)))
                    .andExpect(status().isInternalServerError());
        }
    }
}

