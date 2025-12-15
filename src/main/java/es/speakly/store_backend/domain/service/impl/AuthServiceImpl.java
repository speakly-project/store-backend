package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.LoginUserDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.User;
import es.speakly.store_backend.domain.repository.AuthRepository;
import es.speakly.store_backend.domain.repository.UserRepository;
import es.speakly.store_backend.domain.service.AuthService;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import es.speakly.store_backend.mappers.UserMapper;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    public AuthServiceImpl(UserRepository userRepository, AuthRepository authRepository) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
    }


    @Override
    public LoginUserDto getUserFromToken(String token) {
        return authRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Invalid token"));
    }

    @Override
    @Transactional
    public String createTokenForUser(UserDto user) {
        UserDto userDb = userRepository.findByEmail(user.email()).orElseThrow(() -> new ResourceNotFoundException("User with email " + user.email() + " not found"));

        if (!BCrypt.checkpw(user.password(), userDb.password())) {
            throw new BusinessException("Invalid password");
        }
        return authRepository.createTokenForUser(userDb.id()).toString();
    }

    @Override
    @Transactional
    public void deleteToken(String token) {
        if (token == null || token.isBlank()) {
            throw new BusinessException("Token cannot be null");
        }
        authRepository.deleteToken(token);

    }
}
