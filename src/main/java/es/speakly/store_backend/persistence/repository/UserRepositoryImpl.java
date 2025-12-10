package es.speakly.store_backend.persistence.repository;


import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.repository.UserRepository;
import es.speakly.store_backend.mappers.UserMapper;
import es.speakly.store_backend.persistence.dao.UserDao;
import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final UserDao userDao;

    public UserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public Page<UserDto> findAll(int pageNumber, int pageSize) {
        List<UserJpaEntity> entities = userDao.findAll(pageNumber, pageSize);
        List<UserDto> userDtos = entities.stream()
                .map(UserMapper::fromUserEntityToUserDto)
                .toList();
        long totalElements = userDao.count();
        return new Page<>(userDtos, pageNumber, pageSize, totalElements);
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userDao.findById(id)
                .map(UserMapper::fromUserEntityToUserDto);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return userDao.findByEmail(email)
                .map(UserMapper::fromUserEntityToUserDto);
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return userDao.findByUsername(username)
                .map(UserMapper::fromUserEntityToUserDto);
    }

    @Override
    public UserDto save(UserDto user) {
        UserJpaEntity entity = UserMapper.fromUserDtoToUserEntity(user);

        if (user.id() == null) {
            return UserMapper.fromUserEntityToUserDto(userDao.insert(entity));
        }
        return UserMapper.fromUserEntityToUserDto(userDao.update(entity));
    }

    @Override
    public void delete(Long id) {
        userDao.deleteById(id);
    }
}