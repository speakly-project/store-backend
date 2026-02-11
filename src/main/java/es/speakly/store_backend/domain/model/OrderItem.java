package es.speakly.store_backend.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrderItem {
    private Long id;
    private Course Course;
    private Long quantity;
    private BigDecimal price;

    public OrderItem(Long id, Course course, Long quantity, BigDecimal price) {
        this.id = id;
        Course = course;
        this.quantity = quantity;
        this.price = price;
        //this.price = calculatePrice();
    }

//    private BigDecimal calculatePrice() {
//        if (this.price == null) return null;
//
//        BigDecimal ivaMultiplier = new BigDecimal("1.21");
//        return this.price.multiply(ivaMultiplier).setScale(2, RoundingMode.HALF_UP);
//    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Course getCourse() {
        return Course;
    }

    public void setCourse(Course course) {
        Course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
