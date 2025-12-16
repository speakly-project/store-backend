package es.speakly.store_backend.mappers;

import es.speakly.store_backend.controller.webmodel.request.CourseInsertRequest;
import es.speakly.store_backend.controller.webmodel.request.CourseUpdateRequest;
import es.speakly.store_backend.controller.webmodel.response.CourseDetailResponse;
import es.speakly.store_backend.controller.webmodel.response.CourseSummaryResponse;
import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.domain.model.Course;
import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;
import es.speakly.store_backend.domain.dto.CourseDto;

public class CourseMapper {

    private static CourseMapper INSTANCE;

    private CourseMapper() {
    }

    public static CourseMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CourseMapper();
        }
        return INSTANCE;
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
                courseDto.level()
        );
    }


    public static CourseDetailResponse fromCourseDtoToCourseDetailResponse(CourseDto courseDto) {
        if (courseDto == null) {
            return null;
        }
        return new CourseDetailResponse(
                courseDto.id(),
                courseDto.title(),
                courseDto.description(),
                courseDto.price(),
                courseDto.language(),
                courseDto.level(),
                courseDto.teacherId()
        );
    }

    public static CourseDto fromCourseInsertRequestToCourseDto(CourseInsertRequest request) {
        if (request == null) {
            return null;
        }
        return new CourseDto(
                null,
                request.title(),
                request.description(),
                request.price(),
                request.language(),
                request.level(),
                request.teacherId()
        );
    }

    public static CourseDto fromCourseUpdateRequestToCourseDto(CourseUpdateRequest request) {
        if (request == null) {
            return null;
        }
        return new CourseDto(
                request.id(),
                request.title(),
                request.description(),
                request.price(),
                request.language(),
                request.level(),
                request.teacherId()
        );
    }

    public static Course fromCourseEntityToCourse(CourseJpaEntity courseEntity) {
        if (courseEntity == null) {
            return null;
        }

        return new Course(
                courseEntity.getId(),
                courseEntity.getTitle(),
                courseEntity.getDescription(),
                courseEntity.getPrice(),
                courseEntity.getLanguage(),
                courseEntity.getLevel(),
                courseEntity.getUser() != null ? courseEntity.getUser().getId() : null
        );
    }


    public static CourseDto fromCourseToCourseDto(Course course) {
        if (course == null) {
            return null;
        }

        return new CourseDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getPrice(),
                course.getLanguage(),
                course.getLevel(),
                course.getTeacherId()
        );
    }

    public static Course fromCourseDtoToCourse(CourseDto courseDto) {
        if (courseDto == null) {
            return null;
        }

        return new Course(
                courseDto.id(),
                courseDto.title(),
                courseDto.description(),
                courseDto.price(),
                courseDto.language(),
                courseDto.level(),
                courseDto.teacherId()
        );
    }



    public static CourseJpaEntity fromCourseDtoToCourseEntity(CourseDto courseDto) {
        if (courseDto == null) {
            return null;
        }
//        CourseJpaEntity courseEntity = new CourseJpaEntity();
//        courseEntity.setId(courseDto.id());
//        courseEntity.setTitle(courseDto.title());
//        courseEntity.setDescription(courseDto.description());
//        courseEntity.setPrice(courseDto.price());
//        courseEntity.setLanguage(courseDto.language());
//        courseEntity.setLevel(courseDto.level());
//        return courseEntity;
        return new CourseJpaEntity(
                courseDto.id(),
                courseDto.title(),
                courseDto.description(),
                courseDto.price(),
                courseDto.language(),
                courseDto.level(),
                new UserJpaEntity(courseDto.teacherId(), null, null, null, null, null, null, UserRole.USER)
        );
    }

    public static CourseDto fromCourseEntityToCourseDto(CourseJpaEntity courseEntity) {
        if (courseEntity == null) {
            return null;
        }
        return new CourseDto(
                courseEntity.getId(),
                courseEntity.getTitle(),
                courseEntity.getDescription(),
                courseEntity.getPrice(),
                courseEntity.getLanguage(),
                courseEntity.getLevel(),
                courseEntity.getUser() != null ? courseEntity.getUser().getId() : null
        );
    }


}