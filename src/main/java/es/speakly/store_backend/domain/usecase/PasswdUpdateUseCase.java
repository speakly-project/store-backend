package es.speakly.store_backend.domain.usecase;

import es.speakly.store_backend.domain.dto.UserDto;

public interface PasswdUpdateUseCase {
    void updatePassword(Long userId, String oldPassword, String newPassword);
}
