package es.speakly.store_backend.persistence.dao.impl.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "sessions")
public class SessionJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createAt;

    public SessionJpaEntity() {}

    public SessionJpaEntity(String token, UserJpaEntity user, LocalDateTime createAt) {
        this.user = user;
        this.token = token;
        this.createAt = createAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserJpaEntity getUser() {
        return user;
    }

    public void setUser(UserJpaEntity user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createAt = created;
    }


}
