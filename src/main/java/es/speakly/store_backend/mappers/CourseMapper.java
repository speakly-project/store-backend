package es.speakly.store_backend.mappers;

import es.speakly.store_backend.controller.webmodel.request.CourseInsertRequest;
import es.speakly.store_backend.controller.webmodel.request.CourseUpdateRequest;
import es.speakly.store_backend.controller.webmodel.response.CourseSummaryResponse;
import es.speakly.store_backend.domain.model.Course;
import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;
import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.UserDto;

import java.util.List;

public class CourseMapper {

    private CourseMapper() {
    }

    public static CourseDto fromCourseInsertRequestToCourseDto(CourseInsertRequest request) {
        if (request == null) {
            return null;
        }
        UserDto teacher = null;
        if (request.teacherId() != null) {
            teacher = new UserDto(
                    request.teacherId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    List.of(),
                    UserRole.USER
            );
        }
        return new CourseDto(
                null,
                request.title(),
                request.description(),
                request.price(),
                request.language(),
                request.level(),
                teacher,
                request.duration(),
                request.createdAt()
        );
    }

    public static CourseDto fromCourseUpdateRequestToCourseDto(CourseUpdateRequest request) {
        if (request == null) {
            return null;
        }
        UserDto teacher = null;
        if (request.teacherId() != null) {
            teacher = new UserDto(
                    request.teacherId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    List.of(),
                    UserRole.USER
            );
        }
        return new CourseDto(
                request.id(),
                request.title(),
                request.description(),
                request.price(),
                request.language(),
                request.level(),
                teacher,
                request.duration(),
                null
        );
    }

    public static Course fromCourseDtoToCourse(CourseDto courseDto) {
        if (courseDto == null) {
            return null;
        }

        Long teacherId = courseDto.teacher() != null ? courseDto.teacher().id() : null;

        return new Course(
                courseDto.id(),
                courseDto.title(),
                courseDto.description(),
                courseDto.price(),
                courseDto.language(),
                courseDto.level(),
                teacherId,
                courseDto.duration(),
                courseDto.createdAt()
        );
    }

    public static CourseDto fromCourseToCourseDto(Course course) {
        if (course == null) {
            return null;
        }

        UserDto teacher = null;
        if (course.getTeacherId() != null) {
            teacher = new UserDto(
                    course.getTeacherId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    List.of(),
                    UserRole.USER
            );
        }

        return new CourseDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getPrice(),
                course.getLanguage(),
                course.getLevel(),
                teacher,
                course.getDuration(),
                course.getCreatedAt()
        );
    }

    public static CourseDto fromCourseEntityToCourseDto(CourseJpaEntity courseEntity) {
        if (courseEntity == null) {
            return null;
        }
        UserDto teacher = null;
        if (courseEntity.getTeacher() != null) {
            // Evita exponer datos sensibles
            teacher = new UserDto(
                    courseEntity.getTeacher().getId(),
                    courseEntity.getTeacher().getUsername(),
                    courseEntity.getTeacher().getEmail(),
                    courseEntity.getTeacher().getProfilePictureUrl(),
                    null,
                    courseEntity.getTeacher().getCreatedAt(),
                    List.of(),
                    null
            );
        }
        return new CourseDto(
                courseEntity.getId(),
                courseEntity.getTitle(),
                courseEntity.getDescription(),
                courseEntity.getPrice(),
                courseEntity.getLanguage(),
                courseEntity.getLevel(),
                teacher,
                courseEntity.getDuration(),
                courseEntity.getCreatedAt()
        );
    }

    public static CourseJpaEntity fromCourseDtoToCourseEntity(CourseDto courseDto) {
        if (courseDto == null) {
            return null;
        }
        UserJpaEntity teacher = null;
        if (courseDto.teacher() != null && courseDto.teacher().id() != null) {
            teacher = new UserJpaEntity(
                    courseDto.teacher().id(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    UserRole.USER
            );
        }
        return new CourseJpaEntity(
                courseDto.id(),
                courseDto.title(),
                courseDto.description(),
                courseDto.price(),
                courseDto.language(),
                courseDto.level(),
                courseDto.duration(),
                teacher
        );
    }
    public static CourseSummaryResponse fromCourseDtoToCourseSummaryResponse(CourseDto courseDto) {
        if (courseDto == null) {
            return null;
        }
        return new CourseSummaryResponse(
                courseDto.title(),
                courseDto.description(),
                courseDto.price(),
                courseDto.language(),
                courseDto.level(),
                courseDto.duration(),
                courseDto.createdAt()
        );
    }
}