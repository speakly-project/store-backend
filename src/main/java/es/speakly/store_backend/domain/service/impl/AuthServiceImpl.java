package es.speakly.store_backend.domain.service.impl;

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
    public UserDto getUserFromToken(String token) {
        return authRepository.findByToken(token).map(UserMapper::fromUserDtoToUser).map(UserMapper::fromUserToUserDto).orElse(null);
    }

    @Override
    @Transactional
    public String createTokenForUser(UserDto user) {
        UserDto newUser = userRepository.findByEmail(user.email())
                .map(UserMapper::fromUserDtoToUser).map(UserMapper::fromUserToUserDto).orElseThrow(() -> new ResourceNotFoundException("User with email " + user.email() + " not found"));
        boolean isValid = BCrypt.checkpw(user.password(), newUser.password());
        if (!isValid) {
            throw new BusinessException("Invalid password");
        }
        return authRepository.createTokenForUser(newUser.id()).toString();
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
