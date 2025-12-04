package es.speakly.store_backend.domain.service;

import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Page;

import java.util.Optional;

public interface UserService {
    Page<UserDto> getAll(int pageNumber, int pageSize);
    Optional<UserDto> findById(Long id);
    Optional<UserDto> findByEmail(String email);
    Optional<UserDto> findByUsername(String username);
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user);
    void delete(Long id);
}
