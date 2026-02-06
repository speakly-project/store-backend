package es.speakly.store_backend.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private User user;
    private OrderStatus orderStatus;
    private List<OrderItem> orderItems;
    private BigDecimal totalPrice;
    private LocalDateTime paidDate;
    private LocalDateTime createAt;

    public Order(Long id, User user, OrderStatus orderStatus, List<OrderItem> orderItems, LocalDateTime paidDate, LocalDateTime createAt) {
        this.id = id;
        this.user = user;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
        this.totalPrice = calculateTotalPrice();
        this.paidDate = paidDate;
        this.createAt = createAt;
    }

    private BigDecimal calculateTotalPrice() {
        if (orderItems == null || orderItems.isEmpty()) return BigDecimal.ZERO;
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
