package es.speakly.store_backend.mappers;

import es.speakly.store_backend.controller.webmodel.response.OrderItemResponse;
import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.OrderItemDto;
import es.speakly.store_backend.domain.model.Course;
import es.speakly.store_backend.domain.model.OrderItem;
import es.speakly.store_backend.domain.model.OrderStatus;
import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.OrderItemJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.OrderJpaEntity;

public class OrderItemMapper {
    public static OrderItem fromOrderItemDtoToOrderItem(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            return null;
        }

        Course course = CourseMapper.fromCourseDtoToCourse(orderItemDto.course());

        return new OrderItem(
                orderItemDto.id(),
                course,
                orderItemDto.quantity(),
                orderItemDto.price()
        );
    }

    public static OrderItemDto fromOrderItemToOrderItemDto(OrderItem orderItem, OrderStatus orderStatus) {
        if (orderItem == null) {
            return null;
        }

        CourseDto courseDto = CourseMapper.fromCourseToCourseDto(orderItem.getCourse());

        if (orderStatus == OrderStatus.PENDING && orderItem.getCourse() != null) {
            Course course = orderItem.getCourse();
            return new OrderItemDto(
                    orderItem.getId(),
                    courseDto,
                    orderItem.getQuantity(),
                    orderItem.getPrice()
            );
        } else {
            return new OrderItemDto(
                    orderItem.getId(),
                    courseDto,
                    orderItem.getQuantity(),
                    orderItem.getPrice()
            );
        }
    }

    public static OrderItemDto fromOrderItemToOrderItemDto(OrderItem orderItem) {
        return fromOrderItemToOrderItemDto(orderItem, OrderStatus.PAYED);
    }

    public static OrderItemJpaEntity fromOrderItemToOrderItemEntity(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            return null;
        }

        CourseJpaEntity course = CourseMapper.fromCourseDtoToCourseEntity(orderItemDto.course());

        return new OrderItemJpaEntity(
                orderItemDto.id(),
                null,
                course,
                orderItemDto.price(),
                orderItemDto.quantity()
        );
    }

    public static OrderItemResponse fromOderItemDtoToOrderItemResponse(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            return null;
        }

        CourseDto courseDto = orderItemDto.course();

        return new OrderItemResponse(
                orderItemDto.id(),
                CourseMapper.fromCourseDtoToCourseSummaryResponse(courseDto),
                orderItemDto.quantity(),
                orderItemDto.price()
        );
    }
}
