package es.speakly.store_backend.domain.repository;

import com.speakly.storebackend.domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User save(User user);
    void delete(UUID id);
}