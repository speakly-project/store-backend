package es.speakly.store_backend.domain.repository;

import es.speakly.store_backend.domain.dto.UserDto;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository {
    Optional<UserDto> findByToken(String token);
    UUID createTokenForUser(Long userId);
    void deleteToken(String token);
}
