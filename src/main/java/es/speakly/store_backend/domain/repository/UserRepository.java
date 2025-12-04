package es.speakly.store_backend.domain.repository;


import es.speakly.store_backend.domain.model.User;

import java.util.Optional;


public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User save(User user);
    void delete(Long id);
}