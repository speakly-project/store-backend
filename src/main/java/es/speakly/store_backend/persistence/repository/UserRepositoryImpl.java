package es.speakly.store_backend.persistence.repository;


import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.UserRepository;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {


    @Override
    public Page<UserDto> findAll(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public UserDto save(UserDto user) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}