package es.speakly.store_backend.domain.service;

import es.speakly.store_backend.domain.dto.UserDto;

public interface AuthService {
    UserDto getUserFromToken(String token);
    String createTokenForUser(UserDto user);
    void deleteToken(String token);

}
