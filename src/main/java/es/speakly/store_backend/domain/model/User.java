package es.speakly.store_backend.domain.model;


import java.time.LocalDateTime;
import java.util.List;


public class User {
    
    private Long id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private String encryptedPassword;
    private LocalDateTime createdAt;
    private List<Course> coursesTaken;

    public User(Long id, String username, String email, String profilePictureUrl, String encryptedPassword, LocalDateTime createdAt, List<Course> coursesTaken) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.encryptedPassword = encryptedPassword;
        this.createdAt = createdAt;
        this.coursesTaken = coursesTaken;
    }


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

    public List<Course> getCoursesTaken() {
        return coursesTaken;
    }

    public void setCoursesTaken(List<Course> coursesTaken) {
        this.coursesTaken = coursesTaken;
    }
}