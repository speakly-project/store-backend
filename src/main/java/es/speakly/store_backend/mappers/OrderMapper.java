package es.speakly.store_backend.mappers;

import es.speakly.store_backend.controller.webmodel.request.OrderUpdateRequest;
import es.speakly.store_backend.controller.webmodel.response.OrderItemResponse;
import es.speakly.store_backend.controller.webmodel.response.OrderResponse;
import es.speakly.store_backend.domain.dto.OrderDto;
import es.speakly.store_backend.domain.dto.OrderItemDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Order;
import es.speakly.store_backend.domain.model.OrderItem;
import es.speakly.store_backend.domain.model.OrderStatus;
import es.speakly.store_backend.persistence.dao.impl.entity.OrderItemJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.OrderJpaEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static Order fromOrderDtoToOrder(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }

        List<OrderItem> orderItems = new ArrayList<>();
        if (orderDto.items() != null) {
            orderItems = orderDto.items().stream()
                    .map(OrderItemMapper::fromOrderItemDtoToOrderItem)
                    .toList();
        }

        return new Order(
                orderDto.id(),
                UserMapper.fromUserDtoToUser(orderDto.user()),
                orderDto.status(),
                orderItems,
                orderDto.paidDate(),
                orderDto.createdAt()
        );
    }

    public static OrderDto fromOrderToOrderDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderStatus orderStatus = order.getOrderStatus();
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        if (order.getOrderItems() != null) {
            orderItemDtos = order.getOrderItems().stream()
                    .map(item -> OrderItemMapper.fromOrderItemToOrderItemDto(item, orderStatus))
                    .toList();
        }

        return new OrderDto(
                order.getId(),
                UserMapper.fromUserToUserDto(order.getUser()),
                order.getOrderStatus(),
                orderItemDtos,
                order.getTotalPrice(),
                order.getPaidDate(),
                order.getCreateAt()
        );
    }

    public static OrderJpaEntity fromOrderDtoToOrderEntity(OrderDto order) {
        if (order == null) {
            return null;
        }

        List<OrderItemJpaEntity> orderItemJpaEntities = new ArrayList<>();
        if (order.items() != null) {
            orderItemJpaEntities = order.items().stream()
                    .map(OrderItemMapper::fromOrderItemToOrderItemEntity)
                    .toList();
        }

        return new OrderJpaEntity(
                order.id(),
                UserMapper.fromUserDtoToUserEntity(order.user()),
                order.status(),
                orderItemJpaEntities
        );
    }

    public static OrderDto fromOrderEntityToOrderDto(OrderJpaEntity orderEntity) {
        if (orderEntity == null) {
            return null;
        }

        OrderStatus orderStatus = orderEntity.getOrderStatus();
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        if (orderEntity.getOrderItems() != null) {
            orderItemDtos = orderEntity.getOrderItems().stream()
                    .map(item -> OrderItemMapper.fromOrderItemToOrderItemDto(
                            new OrderItem(
                                    item.getId(),
                                    CourseMapper.fromCourseDtoToCourse(CourseMapper.fromCourseEntityToCourseDto(item.getCourse())),
                                    item.getQuantity(),
                                    item.getBasePrice()
                            ), orderStatus))
                    .toList();
        }

        return new OrderDto(
                orderEntity.getId(),
                UserMapper.fromUserEntityToUserDto(orderEntity.getUser()),
                orderEntity.getOrderStatus(),
                orderItemDtos,
                orderEntity.getOrderItems().stream()
                        .map(item -> item.getBasePrice().multiply(new java.math.BigDecimal(item.getQuantity())))
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add),
                orderEntity.getPaidDate(),
                orderEntity.getCreatedAt()
        );
    }

    public static OrderResponse fromOrderDtoToOrderResponse(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }

        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        if (orderDto.items() != null) {
            orderItemResponses = orderDto.items().stream()
                    .map(OrderItemMapper::fromOderItemDtoToOrderItemResponse)
                    .toList();
        }


        return new OrderResponse(
                orderDto.id(),
                orderDto.user().id(),
                orderDto.status(),
                orderItemResponses,
                orderDto.totalPrice(),
                orderDto.createdAt()
        );
    }

    public static OrderDto fromOrderUpdateRequestToOrderDto(OrderUpdateRequest orderUpdateRequest, UserDto userDto) {
        if (orderUpdateRequest == null) {
            return null;
        }

        List<OrderItemDto> courses = new ArrayList<>();
        if (orderUpdateRequest.courseIds() != null) {
            courses = Arrays.stream(orderUpdateRequest.courseIds())
                    .map(id -> new OrderItemDto(id, null, null, null))
                    .collect(Collectors.toList());
        }


        return new OrderDto(
                null,
                userDto,
                OrderStatus.PENDING,
                courses,
                null,
                null,
                null
        );
    }
}
