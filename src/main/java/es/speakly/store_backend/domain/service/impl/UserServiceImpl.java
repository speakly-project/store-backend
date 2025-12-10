package es.speakly.store_backend.domain.service.impl;

import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.UserRepository;
import es.speakly.store_backend.domain.service.UserService;
import jakarta.transaction.Transactional;

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
        Page<UserDto> movieEntityPage =  userRepository
                .findAll(pageNumber, pageSize);
        List<UserDto> itemsDto = movieEntityPage.data()
                .stream()
                .toList();
        return new Page<>(
                itemsDto,
                movieEntityPage.pageNumber(),
                movieEntityPage.pageSize(),
                movieEntityPage.totalElements()
        );
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto user) {
        if (findByUsername(user.username()).isPresent()) {
            throw new IllegalArgumentException("Username " + user.username() + " is already taken");
        }
        if (findByEmail(user.email()).isPresent()) {
            throw new IllegalArgumentException("Email " + user.email() + " is already taken");
        }
        user.coursesTaken().forEach(course -> {
            if (course.id() == null || course.id() <= 0) {
                throw new IllegalArgumentException("Course id " + course.id() + " is not valid");
            }
        });

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto user) {
        userRepository.findById(user.id()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.findByEmail(user.email()).filter(b->!b.id().equals(user.id())).ifPresent(b->{
            throw new IllegalArgumentException("User with " + user.email() + " already exists");
        });
        userRepository.findByUsername(user.username()).filter(b->!b.id().equals(user.id())).ifPresent(b->{
            throw new IllegalArgumentException("User with " + user.username() + " already exists");
        });
        user.coursesTaken().forEach(course -> {
            if (course.id() == null || course.id() <= 0) {
                throw new IllegalArgumentException("Course id " + course.id() + " is not valid");
            }
        });
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<UserDto> userDto = userRepository.findById(id);
        if (userDto.isEmpty()){
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }
        userRepository.delete(id);
    }
}
