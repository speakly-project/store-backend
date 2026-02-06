package es.speakly.store_backend.persistence.repository;

import es.speakly.store_backend.domain.dto.OrderDto;
import es.speakly.store_backend.domain.model.OrderStatus;
import es.speakly.store_backend.domain.repository.OrderRepository;
import es.speakly.store_backend.mappers.OrderMapper;
import es.speakly.store_backend.persistence.dao.OrderDao;
import es.speakly.store_backend.persistence.dao.impl.entity.OrderJpaEntity;

import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {
    private final OrderDao orderJpaDao;

    public OrderRepositoryImpl(OrderDao orderJpaDao) {
        this.orderJpaDao = orderJpaDao;
    }

    @Override
    public OrderDto save(OrderDto orderDto) {

        OrderJpaEntity entity = OrderMapper.fromOrderDtoToOrderEntity(orderDto);

        if (orderDto.id() == null) {
            return OrderMapper.fromOrderEntityToOrderDto(orderJpaDao.insert(entity));
        } else {
            return OrderMapper.fromOrderEntityToOrderDto(orderJpaDao.update(entity));
        }
    }

    @Override
    public void delete(Long orderId) {
        orderJpaDao.deleteById(orderId);
    }

    @Override
    public Optional<OrderStatus> getStatus(Long orderId) {
        return orderJpaDao.findOrderStatus(orderId);
    }

    @Override
    public Optional<OrderDto> getActiveOrder(Long userId) {
        return orderJpaDao.findActiveOrderByUserId(userId)
                .map(OrderMapper::fromOrderEntityToOrderDto);
    }
}
