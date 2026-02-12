package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.OrderDto;
import es.speakly.store_backend.domain.dto.OrderItemDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Order;
import es.speakly.store_backend.domain.model.OrderStatus;
import es.speakly.store_backend.domain.repository.OrderRepository;
import es.speakly.store_backend.domain.service.CartService;
import es.speakly.store_backend.domain.service.CourseService;
import es.speakly.store_backend.domain.service.UserService;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import es.speakly.store_backend.exceptions.ValidationException;
import es.speakly.store_backend.nanoServices.payment.CardPaymentService;
import es.speakly.store_backend.mappers.OrderMapper;
import es.speakly.store_backend.mappers.UserMapper;
import es.speakly.store_backend.nanoServices.payment.dtos.OriginDto;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartServiceImpl implements CartService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CourseService courseService;
    private final CardPaymentService cardPaymentService;

    public CartServiceImpl(OrderRepository orderRepository, UserService userService, CourseService courseService, CardPaymentService cardPaymentService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.courseService = courseService;
        this.cardPaymentService = cardPaymentService;
    }


    @Override
    @Transactional
    public void deleteCart(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("ID must be a positive number");
        }

        userService.getById(id);
        OrderDto activeCart = getCart(id);

        if (activeCart.status() != OrderStatus.PENDING) {
            throw new BusinessException("Cart must be in PENDING status to be deleted");
        }

        OrderDto clearedCart = new OrderDto(
                activeCart.id(),
                activeCart.user(),
                OrderStatus.PENDING,
                new ArrayList<>(),
                BigDecimal.ZERO,
                null,
                activeCart.createdAt()
        );

        orderRepository.save(clearedCart);
    }

    @Override
    @Transactional
    public void createCart(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("ID must be a positive number");
        }

        UserDto user = userService.getById(id);

        Order newCart = new Order(
                null,
                UserMapper.fromUserDtoToUser(user),
                OrderStatus.PENDING,
                new ArrayList<>(),
                null,
                LocalDateTime.now()
        );

        OrderDto newCartDto = OrderMapper.fromOrderToOrderDto(newCart);
        orderRepository.save(newCartDto);
    }

    @Override
    @Transactional
    public OrderDto getCart(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("ID must be a positive number");
        }

        userService.getById(id);

        return orderRepository.getActiveOrder(id)
                .map(OrderMapper::fromOrderDtoToOrder)
                .map(OrderMapper::fromOrderToOrderDto)
                .orElseGet(() -> {
                    createCart(id);
                    return orderRepository.getActiveOrder(id)
                            .map(OrderMapper::fromOrderDtoToOrder)
                            .map(OrderMapper::fromOrderToOrderDto)
                            .orElseThrow(() -> new BusinessException("Failed to create cart for user with id: " + id));
                });
    }

    @Override
    public OrderStatus getCartStatus(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("ID must be a positive number");
        }

        return orderRepository.getStatus(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + id));
    }

    @Override
    @Transactional
    public void updatePendingCart(OrderDto orderDto) {
        if (orderDto == null) {
            throw new ValidationException("Order cannot be null");
        }
        if (orderDto.user() == null || orderDto.user().id() == null || orderDto.user().id() <= 0) {
            throw new ValidationException("ID must be a positive number");
        }

        UserDto user = userService.getById(orderDto.user().id());
        OrderDto activeCart = getCart(orderDto.user().id());

        if (activeCart.status() != OrderStatus.PENDING) {
            throw new BusinessException("Cart must be in PENDING status to be updated");
        }

        if (orderDto.items() != null) {
            Set<Long> courseIds = new HashSet<>();
            for (OrderItemDto item : orderDto.items()) {
                if (item.course() == null || item.course().id() == null || item.course().id() <= 0) {
                    throw new ValidationException("Product ID must be a positive number");
                }
                if (item.quantity() == null || item.quantity() < 1) {
                    throw new ValidationException("Quantity must be at least 1");
                }
                if (!courseIds.add(item.course().id())) {
                    throw new ValidationException("Duplicate product in cart: " + item.course().id());
                }
            }
        }

        List<OrderItemDto> resolvedItems = new ArrayList<>();
        if (orderDto.items() != null && !orderDto.items().isEmpty()) {
            for (OrderItemDto item : orderDto.items()) {
                CourseDto fullProduct = courseService.getById(item.course().id());

                BigDecimal basePrice = item.price();
                if (basePrice == null || BigDecimal.ZERO.compareTo(basePrice) == 0) {
                    basePrice = fullProduct.price();
                }

                OrderItemDto resolvedItem = new OrderItemDto(
                        item.id(),
                        fullProduct,
                        item.quantity(),
                        basePrice
                );
                resolvedItems.add(resolvedItem);
            }
        }

        OrderDto orderWithResolvedItems = new OrderDto(
                orderDto.id(),
                orderDto.user(),
                orderDto.status(),
                resolvedItems,
                orderDto.totalPrice(),
                orderDto.paidDate(),
                orderDto.createdAt()
        );

        Order orderModel = OrderMapper.fromOrderDtoToOrder(orderWithResolvedItems);
        OrderDto orderToUpdate = OrderMapper.fromOrderToOrderDto(orderModel);

        OrderDto updatedCart = new OrderDto(
                activeCart.id(),
                user,
                OrderStatus.PENDING,
                orderToUpdate.items(),
                orderToUpdate.totalPrice(),
                null,
                activeCart.createdAt()
        );

        orderRepository.save(updatedCart);
    }

    @Override
    @Transactional
    public void updateCart(OrderDto orderDto) {
        if (orderDto == null) {
            throw new ValidationException("Order cannot be null");
        }
        if (orderDto.user() == null || orderDto.user().id() == null || orderDto.user().id() <= 0) {
            throw new ValidationException("Client Id must be a valid number");
        }

        UserDto user = userService.getById(orderDto.user().id());
        OrderDto activeCart = getCart(orderDto.user().id());

        OrderStatus currentStatus = activeCart.status();
        OrderStatus newStatus = orderDto.status();

        if (currentStatus == OrderStatus.PAYED) {
            throw new BusinessException("Cannot update a cart that is already " + currentStatus);
        }

        if (currentStatus == OrderStatus.PENDING && newStatus != OrderStatus.PENDING && newStatus != OrderStatus.PROCESSING) {
            throw new BusinessException("Invalid status transition from PENDING to " + newStatus);
        }

        if (currentStatus == OrderStatus.PROCESSING && newStatus != OrderStatus.PROCESSING && newStatus != OrderStatus.PAYED) {
            throw new BusinessException("Invalid status transition from PROCESSING to " + newStatus);
        }

        if (orderDto.items() != null) {
            Set<Long> productIds = new HashSet<>();
            for (OrderItemDto item : orderDto.items()) {
                if (item.course() == null || item.course().id() == null || item.course().id() <= 0) {
                    throw new ValidationException("Product ID must be a positive number");
                }
                if (item.quantity() == null || item.quantity() < 1) {
                    throw new ValidationException("Quantity must be at least 1");
                }
                if (!productIds.add(item.course().id())) {
                    throw new ValidationException("Duplicate product in cart: " + item.course().id());
                }
            }
        }

        List<OrderItemDto> resolvedItems = new ArrayList<>();
        if (orderDto.items() != null && !orderDto.items().isEmpty()) {
            for (OrderItemDto item : orderDto.items()) {
                CourseDto fullProduct = courseService.getById(item.course().id());

                // Si el front no manda precio/basePrice, lo tomamos del curso.
                BigDecimal basePrice = item.price();
                if (basePrice == null || BigDecimal.ZERO.compareTo(basePrice) == 0) {
                    basePrice = fullProduct.price();
                }

                OrderItemDto resolvedItem = new OrderItemDto(
                        item.id(),
                        fullProduct,
                        item.quantity(),
                        basePrice
                );
                resolvedItems.add(resolvedItem);
            }
        }

        OrderDto orderWithResolvedItems = new OrderDto(
                orderDto.id(),
                orderDto.user(),
                orderDto.status(),
                resolvedItems,
                orderDto.totalPrice(),
                orderDto.paidDate(),
                orderDto.createdAt()
        );

        Order orderModel = OrderMapper.fromOrderDtoToOrder(orderWithResolvedItems);
        OrderDto orderToUpdate = OrderMapper.fromOrderToOrderDto(orderModel);

        LocalDateTime paidDate = (orderToUpdate.status() == OrderStatus.PAYED) ? LocalDateTime.now() : null;

        OrderDto updatedCart = new OrderDto(
                activeCart.id(),
                user,
                orderToUpdate.status(),
                orderToUpdate.items(),
                orderToUpdate.totalPrice(),
                paidDate,
                activeCart.createdAt()
        );

        orderRepository.save(updatedCart);
    }

    @Override
    @Transactional
    public void payCart(Long userId, String cardNumber, String expiryDate, String cvv, String fullName) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("User ID must be a positive number");
        }
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new ValidationException("Card number cannot be blank");
        }
        if (expiryDate == null || expiryDate.isBlank()) {
            throw new ValidationException("Expiry date cannot be blank");
        }
        if (cvv == null || cvv.isBlank()) {
            throw new ValidationException("CVV cannot be blank");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new ValidationException("Full name cannot be blank");
        }

        OrderDto activeCart = getCart(userId);

        if (activeCart.status() != OrderStatus.PENDING) {
            throw new BusinessException("Cart must be in PENDING status to be paid");
        }

        if (activeCart.items() == null || activeCart.items().isEmpty()) {
            throw new BusinessException("Cannot pay for an empty cart");
        }

        BigDecimal totalPrice = activeCart.totalPrice();
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Cart total price must be greater than zero");
        }

        // cart a processing
        UserDto user = userService.getById(userId);
        OrderDto processingCart = new OrderDto(
                activeCart.id(),
                user,
                OrderStatus.PROCESSING,
                activeCart.items(),
                activeCart.totalPrice(),
                null,
                activeCart.createdAt()
        );
        orderRepository.save(processingCart);

        // bank api
        try {
            OriginDto origen = new OriginDto(cardNumber, expiryDate, cvv, fullName);
            cardPaymentService.processPayment(origen, totalPrice);
        } catch (Exception e) {
            //PENDING if payment fails
            OrderDto rollbackCart = new OrderDto(
                    activeCart.id(),
                    user,
                    OrderStatus.PENDING,
                    activeCart.items(),
                    activeCart.totalPrice(),
                    null,
                    activeCart.createdAt()
            );
            orderRepository.save(rollbackCart);
            throw new BusinessException("Payment failed: " + e.getMessage());
        }

        OrderDto payedCart = new OrderDto(
                activeCart.id(),
                user,
                OrderStatus.PAYED,
                activeCart.items(),
                activeCart.totalPrice(),
                LocalDateTime.now(),
                activeCart.createdAt()
        );
        orderRepository.save(payedCart);

    }
}
