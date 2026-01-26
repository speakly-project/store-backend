package es.speakly.store_backend.usecase;

import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.repository.UserRepository;
import es.speakly.store_backend.domain.usecase.PasswdUpdateUseCase;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

public class PasswdUpdateUseCaseImpl implements PasswdUpdateUseCase {
    private final UserRepository userRepository;

    public PasswdUpdateUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        UserDto oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " does not exist"));
        
        if (!BCrypt.checkpw(oldPassword, oldUser.password())) {
            throw new BusinessException("Old password is incorrect");
        } else if (BCrypt.checkpw(newPassword, oldUser.password())) {
            throw new BusinessException("New password must be different from the old password");
        }
        
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        UserDto updatedUser = oldUser.withPassword(hashedPassword);
        userRepository.save(updatedUser);
    }
}
