package es.speakly.store_backend.domain.model;

import java.util.UUID;


public class Course {
    
    private Long id;
    private String title;
    private String description;
    private double price;
    private Long languageId;
    private String level;
    private Long teacherId;

    
    public Course(Long id, String title, String description, double price, Long languageId, String level, Long teacherId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.languageId = languageId;
        this.level = level;
        this.teacherId = teacherId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
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
}