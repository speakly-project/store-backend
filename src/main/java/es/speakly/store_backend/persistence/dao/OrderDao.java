package es.speakly.store_backend.persistence.dao;

import es.speakly.store_backend.domain.model.OrderStatus;
import es.speakly.store_backend.persistence.dao.impl.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends GenericDao<OrderJpaEntity> {
    List<OrderJpaEntity> findByUserId(Long userId);
    Optional<OrderJpaEntity> findActiveOrderByUserId(Long userId);
    Optional<OrderStatus> findOrderStatus(Long orderId);
}
