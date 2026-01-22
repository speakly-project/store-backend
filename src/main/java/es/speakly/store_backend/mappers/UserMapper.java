package es.speakly.store_backend.mappers;

import es.speakly.store_backend.controller.webmodel.request.UserInsertRequest;
import es.speakly.store_backend.controller.webmodel.request.UserUpdateNotAdminRequest;
import es.speakly.store_backend.controller.webmodel.request.UserUpdateRequest;
import es.speakly.store_backend.controller.webmodel.response.UserDetailNotAdminResponse;
import es.speakly.store_backend.controller.webmodel.response.UserDetailResponse;
import es.speakly.store_backend.controller.webmodel.response.UserSummaryResponse;
import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.User;
import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static es.speakly.store_backend.domain.model.UserRole.USER;

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
            userDto.profilePictureUrl()
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
            userDto.createdAt(),
            userDto.coursesTaken() != null ?
                    userDto.coursesTaken().stream()
                            .map(CourseMapper::fromCourseDtoToCourseSummaryResponse).toList()
                    : Collections.emptyList(),
            userDto.role()
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
            userInsertRequest.profilePictureUrl(),
            userInsertRequest.password(),
            userInsertRequest.createdAt(),
            userInsertRequest.coursesTakenIds() != null ?
                    Arrays.stream(userInsertRequest.coursesTakenIds())
                            .map(id -> new CourseDto(id, null, null, null, null, null, null, 0, null))
                            .toList() : Collections.emptyList(),
            userInsertRequest.role()
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
            userUpdateRequest.profilePictureUrl(),
            null,
            userUpdateRequest.createAt(),
            userUpdateRequest.coursesIds() != null ?
                    Arrays.stream(userUpdateRequest.coursesIds())
                            .map(id -> new CourseDto(id, null, null, null, null, null, null, 0, null))
                            .toList() : Collections.emptyList(),
            userUpdateRequest.role()
        );
    }

    public static UserDto fromUserEntityToUserDto(UserJpaEntity userEntity) {
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
                            .toList() : Collections.emptyList(),
            userEntity.getRole()
        );
    }

    public static UserJpaEntity fromUserDtoToUserEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        // For ManyToMany relationship, we only need to set course IDs
        // The DAO layer will fetch the actual managed entities
        List<CourseJpaEntity> courses = new ArrayList<>();
        if (userDto.coursesTaken() != null) {
            for (CourseDto courseDto : userDto.coursesTaken()) {
                CourseJpaEntity courseEntity = new CourseJpaEntity();
                courseEntity.setId(courseDto.id());
                courses.add(courseEntity);
            }
        }

        return new UserJpaEntity(
                userDto.id(),
                userDto.username(),
                userDto.email(),
                userDto.profilePictureUrl(),
                userDto.password(),
                userDto.createdAt(),
                courses,
                userDto.role()
        );
    }

    public static UserDto fromUserToUserDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getProfilePictureUrl(),
            user.getEncryptedPassword(),
            user.getCreatedAt(),
            user.getCoursesTaken() != null ?
                    user.getCoursesTaken().stream()
                            .map(CourseMapper::fromCourseToCourseDto)
                            .toList() : Collections.emptyList(),
            user.getRole()
        );
    }

    public static User fromUserDtoToUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return new User(
            userDto.id(),
            userDto.username(),
            userDto.email(),
            userDto.profilePictureUrl(),
            userDto.password(),
            userDto.createdAt(),
            userDto.coursesTaken() != null ?
                    userDto.coursesTaken().stream()
                            .map(CourseMapper::fromCourseDtoToCourse)
                            .toList() : Collections.emptyList(),
            userDto.role()
        );
    }

    public static UserDto fromUserUpdateNotAdminRequestToUserDto(UserUpdateNotAdminRequest userUpdateNotAdminRequest) {
        if (userUpdateNotAdminRequest == null) {
            return null;
        }
        return new UserDto(
            userUpdateNotAdminRequest.id(),
            userUpdateNotAdminRequest.username(),
            userUpdateNotAdminRequest.email(),
            userUpdateNotAdminRequest.profilePictureUrl(),
            null,
            userUpdateNotAdminRequest.createAt(),
            userUpdateNotAdminRequest.coursesIds() != null ?
                    Arrays.stream(userUpdateNotAdminRequest.coursesIds())
                            .map(id -> new CourseDto(id, null, null, null, null, null, null, 0, null))
                            .toList() : Collections.emptyList(),
            USER
        );
    }

    public static UserDetailNotAdminResponse fromUserDtoToUserDetailNotAdminResponse(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return new UserDetailNotAdminResponse(
            userDto.id(),
            userDto.username(),
            userDto.email(),
            userDto.profilePictureUrl(),
            userDto.createdAt()
        );
    }

}
