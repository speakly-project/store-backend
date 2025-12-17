package es.speakly.store_backend.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;



public class Course {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String language;
    private String level;
    private Long teacherId;
    private int duration;
    private LocalDateTime createdAt;

    public Course(Long id, String title, String description, BigDecimal price,
                  String language, String level, Long teacherId, int duration, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.language = language;
        this.level = level;
        this.teacherId = teacherId;
        this.duration = duration;
        this.createdAt = createdAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLanguageId() {
        return language;
    }

    public void setLanguageId(String language) {
        this.language = language;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getLanguage() {
        return language;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}