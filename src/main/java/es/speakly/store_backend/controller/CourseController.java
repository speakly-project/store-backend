package es.speakly.store_backend.controller;

import es.speakly.store_backend.annotations.Admin;
import es.speakly.store_backend.controller.webmodel.request.CourseInsertRequest;
import es.speakly.store_backend.controller.webmodel.request.CourseUpdateRequest;
import es.speakly.store_backend.controller.webmodel.response.CourseDetailWithTeacherResponse;
import es.speakly.store_backend.controller.webmodel.response.UserSummaryResponse;
import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.CourseFiltersDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.service.CourseService;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.mappers.CourseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/speakly/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<Page<CourseDetailWithTeacherResponse>> findAllCourses(
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) String sortBy
    ) {

        CourseFiltersDto filters = new CourseFiltersDto(language, level, minPrice, maxPrice, sortBy);
        Page<CourseDto> coursesPage = courseService.getAll(pageNumber, pageSize, filters);

        var mapped = coursesPage.data().stream().map(courseDto -> {
            UserSummaryResponse teacher = null;
            if (courseDto.teacher() != null) {
                teacher = new UserSummaryResponse(
                        courseDto.teacher().username(),
                        courseDto.teacher().email(),
                        courseDto.teacher().profilePictureUrl()
                );
            }
            return new CourseDetailWithTeacherResponse(
                    courseDto.id(),
                    courseDto.title(),
                    courseDto.description(),
                    courseDto.price(),
                    courseDto.language(),
                    courseDto.level(),
                    teacher,
                    courseDto.duration(),
                    courseDto.createdAt()
            );
        }).toList();

        Page<CourseDetailWithTeacherResponse> responsePage = new Page<>(
                mapped,
                coursesPage.pageNumber(),
                coursesPage.pageSize(),
                coursesPage.totalElements(),
                coursesPage.totalPages()
        );

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> findCourseById(@PathVariable Long id) {
        CourseDto courseDto = courseService.getById(id);
        return ResponseEntity.ok(courseDto);
    }

    @Admin
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseInsertRequest request) {
        CourseDto courseDto = CourseMapper.fromCourseInsertRequestToCourseDto(request);
        CourseDto createdCourse = courseService.createCourse(courseDto);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @Admin
    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseUpdateRequest request) {

        if (!id.equals(request.id())) {
            throw new BusinessException("ID in path and request body must match");
        }

        CourseDto courseDto = CourseMapper.fromCourseUpdateRequestToCourseDto(request);
        CourseDto updatedCourse = courseService.updateCourse(courseDto);

        return ResponseEntity.ok(updatedCourse);
    }

    @Admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}