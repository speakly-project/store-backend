package es.speakly.store_backend.persistence.dao.Impl.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private String encryptedPassword;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoursesJpaEntity> coursesTaken = new ArrayList<>();

    public UserJpaEntity() {}

    public MovieJpaEntity(Long id, String username, String email, String profilePictureUrl, String encryptedPassword, LocalDateTime createdAt, List<CoursesJpaEntity> coursesTaken) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.encryptedPassword = encryptedPassword;
        this.createdAt = createdAt;
        setCourses(coursesTaken);
    }

    public List<CoursesJpaEntity> getCoursesTaken() {
        return coursesTaken;
    }

    public void setCourses(List<CoursesJpaEntity> coursesTaken) {
        this.coursesTaken.clear();
        for (CoursesJpaEntity courses : coursesTaken) {
            CoursesJpaEntity coursesJpaEntity = new CoursesJpaEntity();
            this.coursesTaken.add(coursesJpaEntity);
        }
    }

    // Getters and setters for other fields
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
    public String getEncryptedPassword() {
        return encryptedPassword;
    }
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }




}