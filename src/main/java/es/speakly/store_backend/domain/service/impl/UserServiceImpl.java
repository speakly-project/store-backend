package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.UserRepository;
import es.speakly.store_backend.domain.service.UserService;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import es.speakly.store_backend.exceptions.ValidationException;
import es.speakly.store_backend.mappers.UserMapper;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Page<UserDto> getAll(int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 1){
            throw new IllegalArgumentException("Page number and size must be greater than 0");
        }
        Page<UserDto> userEntityPage =  userRepository
                .findAll(pageNumber, pageSize);
        List<UserDto> itemsDto = userEntityPage.data()
                .stream().map(UserMapper::fromUserDtoToUser)
                .map(UserMapper::fromUserToUserDto)
                .toList();
        return new Page<>(
                itemsDto,
                userEntityPage.pageNumber(),
                userEntityPage.pageSize(),
                userEntityPage.totalElements()
        );
    }

    @Override
    public UserDto getById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::fromUserDtoToUser)
                .map(UserMapper::fromUserToUserDto).orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public UserDto getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper::fromUserDtoToUser)
                .map(UserMapper::fromUserToUserDto).orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public UserDto getByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper::fromUserDtoToUser)
                .map(UserMapper::fromUserToUserDto).orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto user) {
        if (userRepository.findByUsername(user.username()).isPresent()) {
            throw new BusinessException("Username " + user.username() + " is already taken");
        }
        if (userRepository.findByEmail(user.email()).isPresent()) {
            throw new BusinessException("Email " + user.email() + " is already taken");
        }
        user.coursesTaken().forEach(course -> {
            if (course.id() == null || course.id() <= 0) {
                throw new ValidationException("Course id " + course.id() + " is not valid");
            }
        });

        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        UserDto createdUser = new UserDto(
                null,
                user.username(),
                user.email(),
                user.profilePictureUrl(),
                hashedPassword,
                user.createdAt(),
                user.coursesTaken(),
                user.role());

        return userRepository.save(createdUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto user) {
        userRepository.findById(user.id()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.findByEmail(user.email()).filter(b->!b.id().equals(user.id())).ifPresent(b->{
            throw new BusinessException("User with " + user.email() + " already exists");
        });
        userRepository.findByUsername(user.username()).filter(b->!b.id().equals(user.id())).ifPresent(b->{
            throw new BusinessException("User with " + user.username() + " already exists");
        });
        user.coursesTaken().forEach(course -> {
            if (course.id() == null || course.id() <= 0) {
                throw new ValidationException("Course id " + course.id() + " is not valid");
            }
        });

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<UserDto> userDto = userRepository.findById(id);
        if (userDto.isEmpty()){
            throw new ResourceNotFoundException("User with id " + id + " does not exist");
        }
        userRepository.delete(id);
    }
}
