package es.speakly.store_backend.mappers;

import es.speakly.store_backend.controller.webmodel.request.UserInsertRequest;
import es.speakly.store_backend.controller.webmodel.request.UserUpdateRequest;
import es.speakly.store_backend.controller.webmodel.response.UserDetailResponse;
import es.speakly.store_backend.controller.webmodel.response.UserSummaryResponse;
import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Course;
import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;

import java.util.Arrays;
import java.util.Collections;

public class UserMapper {
    private static UserMapper INSTANCE;

    private UserMapper() {
    }

    public static UserMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserMapper();
        }
        return INSTANCE;
    }

    public static UserSummaryResponse fromUserDtoToUserSummaryResponse(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return new UserSummaryResponse(
            userDto.username(),
            userDto.email(),
            userDto.profilePictureUrl(),
            userDto.password(),
            userDto.createdAt()
        );
    }

    public static UserDetailResponse fromUserDtoToUserDetailResponse(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return new UserDetailResponse(
            userDto.id(),
            userDto.username(),
            userDto.email(),
            userDto.profilePictureUrl(),
            userDto.password(),
            userDto.createdAt(),
            userDto.coursesTaken() != null ?
                    userDto.coursesTaken().stream()
                            .map(CourseMapper::fromCourseDtoToCourseSummaryResponse).toList()
                    : Collections.emptyList()
        );
    }

    public static UserDto fromUserInsertRequestToUserDto(UserInsertRequest userInsertRequest) {
        if (userInsertRequest == null) {
            return null;
        }
        return new UserDto(
            null,
            userInsertRequest.username(),
            userInsertRequest.email(),
            userInsertRequest.password(),
            userInsertRequest.profilePictureUrl(),
            userInsertRequest.createdAt(),
            userInsertRequest.coursesTakenIds() != null ?
                    Arrays.stream(userInsertRequest.coursesTakenIds())
                            .map(id -> new CourseDto(id, null, null, null, null, null, null))
                            .toList() : Collections.emptyList()
        );
    }

    public static UserDto fromUserUpdateRequestToUserDto(UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null) {
            return null;
        }
        return new UserDto(
            userUpdateRequest.id(),
            userUpdateRequest.username(),
            userUpdateRequest.email(),
            userUpdateRequest.password(),
            userUpdateRequest.profilePictureUrl(),
            userUpdateRequest.createAt(),
            userUpdateRequest.coursesIds() != null ?
                    Arrays.stream(userUpdateRequest.coursesIds())
                            .map(id -> new CourseDto(id, null, null, null, null, null, null))
                            .toList() : Collections.emptyList()
        );
    }

    public UserDto fromUserEntityToUserDto(UserJpaEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new UserDto(
            userEntity.getId(),
            userEntity.getUsername(),
            userEntity.getEmail(),
            userEntity.getProfilePictureUrl(),
            userEntity.getEncryptedPassword(),
            userEntity.getCreatedAt(),
            userEntity.getCoursesTaken() != null ?
                    userEntity.getCoursesTaken().stream()
                            .map(CourseMapper::fromCourseEntityToCourseDto)
                            .toList() : Collections.emptyList()
        );
    }

    public UserJpaEntity fromUserDtoToUserEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return new UserJpaEntity(
            userDto.id(),
            userDto.username(),
            userDto.email(),
            userDto.profilePictureUrl(),
            userDto.password(),
            userDto.createdAt(),
            userDto.coursesTaken() != null ?
                    userDto.coursesTaken().stream()
                            .map(CourseMapper::fromCourseDtoToCourseEntity)
                            .toList() : Collections.emptyList()
        );
    }

}
