package es.speakly.store_backend.persistence.dao.jpa.impl;

import es.speakly.store_backend.domain.model.OrderStatus;
import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import es.speakly.store_backend.persistence.dao.impl.OrderJpaDaoImpl;
import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.OrderJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(OrderJpaDaoImpl.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderJpaDaoImplTest {

    @Autowired
    private OrderJpaDaoImpl orderDao;

    @PersistenceContext
    private EntityManager entityManager;

    private UserJpaEntity user;
    private OrderJpaEntity pendingOrder;
    private OrderJpaEntity payedOrder;

    @BeforeEach
    void setUp() {
        user = new UserJpaEntity(
                null,
                "testuser",
                "test@email.com",
                null,
                "encrypted-password",
                LocalDateTime.now(),
                null,
                UserRole.USER
        );
        entityManager.persist(user);
        entityManager.flush();

        CourseJpaEntity course = new CourseJpaEntity(
                null,
                "English A1",
                "Basic English",
                new BigDecimal("19.99"),
                "English",
                "A1",
                10,
                user
        );
        course.setCreatedAt(LocalDateTime.now());
        entityManager.persist(course);
        entityManager.flush();

        pendingOrder = new OrderJpaEntity(null, user, OrderStatus.PENDING, null);
        pendingOrder.setCreatedAt(LocalDateTime.now());
        entityManager.persist(pendingOrder);

        payedOrder = new OrderJpaEntity(null, user, OrderStatus.PAYED, null);
        payedOrder.setCreatedAt(LocalDateTime.now());
        payedOrder.setPaidDate(LocalDateTime.now());
        entityManager.persist(payedOrder);

        entityManager.flush();
        entityManager.clear();
    }

    @Nested
    class FindByIdTests {

        @Test
        void findById_existingOrder_shouldReturnOrder() {
            Optional<OrderJpaEntity> result = orderDao.findById(pendingOrder.getId());

            assertAll(
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(pendingOrder.getId(), result.get().getId()),
                    () -> assertEquals(OrderStatus.PENDING, result.get().getOrderStatus()),
                    () -> assertNotNull(result.get().getUser()),
                    () -> assertEquals(user.getId(), result.get().getUser().getId())
            );
        }

        @Test
        void findById_notFound_shouldReturnEmpty() {
            Optional<OrderJpaEntity> result = orderDao.findById(999999L);

            assertTrue(result.isEmpty());
        }

        @Test
        void findById_nullId_shouldReturnEmpty() {
            Optional<OrderJpaEntity> result = orderDao.findById(null);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindByUserIdTests {

        @Test
        void findByUserId_existingUser_shouldReturnOrders() {
            List<OrderJpaEntity> results = orderDao.findByUserId(user.getId());

            assertAll(
                    () -> assertNotNull(results),
                    () -> assertEquals(2, results.size()),
                    () -> assertTrue(results.stream().allMatch(o -> o.getUser().getId().equals(user.getId())))
            );
        }

        @Test
        void findByUserId_noOrders_shouldReturnEmptyList() {
            UserJpaEntity newUser = new UserJpaEntity(
                    null,
                    "newuser",
                    "new@email.com",
                    null,
                    "password",
                    LocalDateTime.now(),
                    null,
                    UserRole.USER
            );
            entityManager.persist(newUser);
            entityManager.flush();

            List<OrderJpaEntity> results = orderDao.findByUserId(newUser.getId());

            assertAll(
                    () -> assertNotNull(results),
                    () -> assertTrue(results.isEmpty())
            );
        }

        @Test
        void findByUserId_nullId_shouldThrowException() {
            assertThrows(IllegalArgumentException.class, () -> orderDao.findByUserId(null));
        }
    }

    @Nested
    class FindActiveOrderByUserIdTests {

        @Test
        void findActiveOrderByUserId_existingPendingOrder_shouldReturnOrder() {
            Optional<OrderJpaEntity> result = orderDao.findActiveOrderByUserId(user.getId());

            assertAll(
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(OrderStatus.PENDING, result.get().getOrderStatus()),
                    () -> assertEquals(user.getId(), result.get().getUser().getId())
            );
        }

        @Test
        void findActiveOrderByUserId_noPendingOrder_shouldReturnEmpty() {
            // Eliminar la orden pendiente
            entityManager.createQuery("DELETE FROM OrderJpaEntity o WHERE o.orderStatus = :status")
                    .setParameter("status", OrderStatus.PENDING)
                    .executeUpdate();
            entityManager.flush();
            entityManager.clear();

            Optional<OrderJpaEntity> result = orderDao.findActiveOrderByUserId(user.getId());

            assertTrue(result.isEmpty());
        }

        @Test
        void findActiveOrderByUserId_nullId_shouldReturnEmpty() {
            Optional<OrderJpaEntity> result = orderDao.findActiveOrderByUserId(null);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class FindOrderStatusTests {

        @Test
        void findOrderStatus_pendingOrder_shouldReturnPending() {
            Optional<OrderStatus> result = orderDao.findOrderStatus(pendingOrder.getId());

            assertAll(
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(OrderStatus.PENDING, result.get())
            );
        }

        @Test
        void findOrderStatus_payedOrder_shouldReturnPayed() {
            Optional<OrderStatus> result = orderDao.findOrderStatus(payedOrder.getId());

            assertAll(
                    () -> assertTrue(result.isPresent()),
                    () -> assertEquals(OrderStatus.PAYED, result.get())
            );
        }

        @Test
        void findOrderStatus_notFound_shouldReturnEmpty() {
            Optional<OrderStatus> result = orderDao.findOrderStatus(999999L);

            assertTrue(result.isEmpty());
        }

        @Test
        void findOrderStatus_nullId_shouldReturnEmpty() {
            Optional<OrderStatus> result = orderDao.findOrderStatus(null);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class InsertTests {

        @Test
        void insert_validOrder_shouldPersistOrder() {
            OrderJpaEntity newOrder = new OrderJpaEntity(null, user, OrderStatus.PENDING, null);
            newOrder.setCreatedAt(LocalDateTime.now());

            OrderJpaEntity result = orderDao.insert(newOrder);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertNotNull(result.getId()),
                    () -> assertEquals(OrderStatus.PENDING, result.getOrderStatus()),
                    () -> assertEquals(user.getId(), result.getUser().getId())
            );
        }

        @Test
        void insert_nullOrder_shouldThrowException() {
            assertThrows(IllegalArgumentException.class, () -> orderDao.insert(null));
        }

        @Test
        void insert_orderWithoutUser_shouldThrowException() {
            OrderJpaEntity orderWithoutUser = new OrderJpaEntity(null, null, OrderStatus.PENDING, null);

            assertThrows(IllegalArgumentException.class, () -> orderDao.insert(orderWithoutUser));
        }

        @Test
        void insert_orderWithoutStatus_shouldThrowException() {
            OrderJpaEntity orderWithoutStatus = new OrderJpaEntity(null, user, null, null);

            assertThrows(IllegalArgumentException.class, () -> orderDao.insert(orderWithoutStatus));
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void update_validOrder_shouldUpdateOrder() {
            OrderJpaEntity existingOrder = entityManager.find(OrderJpaEntity.class, pendingOrder.getId());
            existingOrder.setOrderStatus(OrderStatus.PROCESSING);

            OrderJpaEntity result = orderDao.update(existingOrder);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(OrderStatus.PROCESSING, result.getOrderStatus())
            );
        }

        @Test
        void update_setPayedWithPaidDate_shouldUpdateBothFields() {
            OrderJpaEntity existingOrder = entityManager.find(OrderJpaEntity.class, pendingOrder.getId());
            LocalDateTime paidDate = LocalDateTime.now();
            existingOrder.setOrderStatus(OrderStatus.PAYED);
            existingOrder.setPaidDate(paidDate);

            OrderJpaEntity result = orderDao.update(existingOrder);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(OrderStatus.PAYED, result.getOrderStatus()),
                    () -> assertNotNull(result.getPaidDate())
            );
        }

        @Test
        void update_nullOrder_shouldThrowException() {
            assertThrows(IllegalArgumentException.class, () -> orderDao.update(null));
        }

        @Test
        void update_orderWithoutId_shouldThrowException() {
            OrderJpaEntity orderWithoutId = new OrderJpaEntity(null, user, OrderStatus.PENDING, null);

            assertThrows(IllegalArgumentException.class, () -> orderDao.update(orderWithoutId));
        }

        @Test
        void update_notFound_shouldThrowException() {
            OrderJpaEntity nonExistentOrder = new OrderJpaEntity(999999L, user, OrderStatus.PENDING, null);

            assertThrows(ResourceNotFoundException.class, () -> orderDao.update(nonExistentOrder));
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void deleteById_existingOrder_shouldDeleteOrder() {
            Long orderId = pendingOrder.getId();

            orderDao.deleteById(orderId);
            entityManager.flush();
            entityManager.clear();

            Optional<OrderJpaEntity> result = orderDao.findById(orderId);
            assertTrue(result.isEmpty());
        }

        @Test
        void deleteById_nullId_shouldThrowException() {
            assertThrows(IllegalArgumentException.class, () -> orderDao.deleteById(null));
        }

        @Test
        void deleteById_notFound_shouldThrowException() {
            assertThrows(ResourceNotFoundException.class, () -> orderDao.deleteById(999999L));
        }
    }

    @Nested
    class FindAllTests {

        @Test
        void findAll_withPagination_shouldReturnCorrectPage() {
            List<OrderJpaEntity> page1 = orderDao.findAll(1, 1);
            List<OrderJpaEntity> page2 = orderDao.findAll(2, 1);

            assertAll(
                    () -> assertNotNull(page1),
                    () -> assertNotNull(page2),
                    () -> assertEquals(1, page1.size()),
                    () -> assertEquals(1, page2.size()),
                    () -> assertNotEquals(page1.getFirst().getId(), page2.getFirst().getId())
            );
        }

        @Test
        void findAll_pageZero_shouldReturnFirstPage() {
            List<OrderJpaEntity> page0 = orderDao.findAll(0, 10);
            List<OrderJpaEntity> page1 = orderDao.findAll(1, 10);

            assertAll(
                    () -> assertNotNull(page0),
                    () -> assertNotNull(page1),
                    () -> assertEquals(page1.size(), page0.size())
            );
        }
    }

    @Nested
    class CountTests {

        @Test
        void count_shouldReturnCorrectCount() {
            long count = orderDao.count();

            assertEquals(2, count);
        }
    }
}

