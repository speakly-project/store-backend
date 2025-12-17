package es.speakly.store_backend.controller;

import es.speakly.store_backend.annotations.Admin;
import es.speakly.store_backend.controller.webmodel.request.CourseInsertRequest;
import es.speakly.store_backend.controller.webmodel.request.CourseUpdateRequest;
import es.speakly.store_backend.controller.webmodel.response.CourseDetailResponse;
import es.speakly.store_backend.controller.webmodel.response.CourseSummaryResponse;
import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.service.CourseService;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.exceptions.DtoValidator;
import es.speakly.store_backend.mappers.CourseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/speakly/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<Page<CourseDetailResponse>> findAllCourses(
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Page<CourseDto> coursesDtoPage = courseService.getAll(pageNumber, pageSize);

        List<CourseDetailResponse> courseDetailResponses = coursesDtoPage.data().stream()
                .map(CourseMapper::fromCourseDtoToCourseDetailResponse).toList();

        Page<CourseDetailResponse> courseDetailResponsePage = new Page<>(
                courseDetailResponses,
                coursesDtoPage.pageNumber(),
                coursesDtoPage.pageSize(),
                coursesDtoPage.totalElements()
        );
        return new ResponseEntity<>(courseDetailResponsePage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailResponse> findCourseById(@PathVariable Long id) {
        CourseDetailResponse courseDetailResponse = CourseMapper.fromCourseDtoToCourseDetailResponse(
                courseService.getById(id));
        return new ResponseEntity<>(courseDetailResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CourseSummaryResponse>> findCoursesByLanguageAndLevel(
            @RequestParam String language,
            @RequestParam String level,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Page<CourseDto> coursesDtoPage = courseService.getByLanguageAndLevel(language, level, pageNumber, pageSize);

        List<CourseSummaryResponse> courseSummaryResponses = coursesDtoPage.data().stream()
                .map(CourseMapper::fromCourseDtoToCourseSummaryResponse).toList();

        Page<CourseSummaryResponse> courseSummaryResponsePage = new Page<>(
                courseSummaryResponses,
                coursesDtoPage.pageNumber(),
                coursesDtoPage.pageSize(),
                coursesDtoPage.totalElements()
        );
        return new ResponseEntity<>(courseSummaryResponsePage, HttpStatus.OK);
    }
    @Admin
    @PostMapping
    public ResponseEntity<CourseDetailResponse> createCourse(@RequestBody CourseInsertRequest courseInsertRequest) {
        CourseDto courseDto = CourseMapper.fromCourseInsertRequestToCourseDto(courseInsertRequest);
        DtoValidator.validate(courseDto);
        CourseDto createdCourseDto = courseService.createCourse(courseDto);
        CourseDetailResponse courseDetailResponse = CourseMapper.fromCourseDtoToCourseDetailResponse(createdCourseDto);
        return new ResponseEntity<>(courseDetailResponse, HttpStatus.CREATED);
    }
    @Admin
    @PutMapping("/{id}")
    public ResponseEntity<CourseDetailResponse> updateCourse(
            @PathVariable("id") Long id,
            @RequestBody CourseUpdateRequest courseUpdateRequest) {
        if (!id.equals(courseUpdateRequest.id())) {
            throw new BusinessException("ID in path and request body must match");
        }
        CourseDto courseDto = CourseMapper.fromCourseUpdateRequestToCourseDto(courseUpdateRequest);
        DtoValidator.validate(courseDto);
        CourseDto updatedCourseDto = courseService.updateCourse(courseDto);
        CourseDetailResponse courseDetailResponse = CourseMapper.fromCourseDtoToCourseDetailResponse(updatedCourseDto);
        return new ResponseEntity<>(courseDetailResponse, HttpStatus.OK);
    }
    @Admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable("id") Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
