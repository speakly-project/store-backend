package es.speakly.store_backend.persistence.repository;

import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.repository.AuthRepository;
import es.speakly.store_backend.mappers.UserMapper;
import es.speakly.store_backend.persistence.dao.AuthDao;

import java.util.Optional;
import java.util.UUID;

public class AuthRepositoryImpl implements AuthRepository {
    private final AuthDao authDao;

    public AuthRepositoryImpl(AuthDao authDao) {
        this.authDao = authDao;
    }

    @Override
    public Optional<UserDto> findByToken(String token) {
        return authDao.findByToken(token).map(UserMapper::fromUserEntityToUserDto);
    }

    @Override
    public UUID createTokenForUser(Long userId) {
        return authDao.createTokenForUser(userId);
    }

    @Override
    public void deleteToken(String token) {
        authDao.deleteToken(token);
    }
}
