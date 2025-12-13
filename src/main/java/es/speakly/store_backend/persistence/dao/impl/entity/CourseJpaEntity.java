package es.speakly.store_backend.persistence.dao.impl.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class CourseJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String language;
    private String level;

    // ManyToOne: The instructor/creator of the course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    // ManyToMany: Students who have enrolled/taken this course
    @ManyToMany(mappedBy = "coursesTaken")
    private List<UserJpaEntity> students = new ArrayList<>();

    public CourseJpaEntity() {}

    public CourseJpaEntity(Long id, String title, String description, BigDecimal price,
                           String language, String level, UserJpaEntity user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.language = language;
        this.level = level;
        this.user = user;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public UserJpaEntity getUser() {
        return user;
    }

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }

    public List<UserJpaEntity> getStudents() {
        return students;
    }

    public void setStudents(List<UserJpaEntity> students) {
        this.students = students;
    }
}