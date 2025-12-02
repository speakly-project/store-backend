package com.speakly.storebackend.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class User {
    
    private UUID id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private String encryptedPassword;
    private LocalDateTime createdAt;
    private Course<Course> coursesTaken;
    
    private User(UUID id, String username, String email, String profilePictureUrl, String encryptedPassword, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.encryptedPassword = encryptedPassword;
        this.createdAt = createdAt;
    }
    
}