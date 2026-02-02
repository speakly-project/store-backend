package es.speakly.store_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.speakly.store_backend.annotations.AuthenticationInterceptor;
import es.speakly.store_backend.controller.webmodel.request.CourseInsertRequest;
import es.speakly.store_backend.controller.webmodel.request.CourseUpdateRequest;
import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.dto.CourseFiltersDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.service.CourseService;
import es.speakly.store_backend.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationInterceptor authenticationInterceptor;

    private CourseDto courseDto;
    private CourseDto courseDto2;
    private CourseDto courseDto3;

    @BeforeEach
    void setUp() throws Exception {
        Mockito.when(authenticationInterceptor.preHandle(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(true);
        courseDto = new CourseDto(
                1L,
                "English A1",
                "Basic English",
                new BigDecimal("19.99"),
                "English",
                "A1",
                new UserDto(10L, null, null, null, null, null, List.of(), null),
                10,
                LocalDateTime.now()
        );
        courseDto2 = new CourseDto(
                2L,
                "Spanish B1",
                "Intermediate Spanish",
                new BigDecimal("29.99"),
                "Spanish",
                "B1",
                new UserDto(11L, null, null, null, null, null, List.of(), null),
                15,
                LocalDateTime.now()
        );
        courseDto3 = new CourseDto(
                3L,
                "German C1",
                "Advanced German",
                new BigDecimal("39.99"),
                "German",
                "C1",
                new UserDto(12L, null, null, null, null, null, List.of(), null),
                20,
                LocalDateTime.now()
        );

    }



    @Nested
    class GetTests {
        @Test
        void shouldReturnAllCourses_withNoFilters() throws Exception {
            List<CourseDto> courses = List.of(courseDto, courseDto2, courseDto3);
            Page<CourseDto> coursePage = new Page<>(courses, 1, 10, courses.size());

            when(courseService.getAll(eq(1), eq(10), any(CourseFiltersDto.class))).thenReturn(coursePage);

            mockMvc.perform(get("/api/speakly/courses")
                            .param("pageNumber", "1")
                            .param("pageSize", "10"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(3))
                    .andExpect(jsonPath("$.pageNumber").value(1))
                    .andExpect(jsonPath("$.pageSize").value(10))
                    .andExpect(jsonPath("$.totalElements").value(3))
                    .andExpect(jsonPath("$.data[0].id").value(courses.getFirst().id()))
                    .andExpect(jsonPath("$.data[2].id").value(courses.getLast().id()));

            verify(courseService).getAll(eq(1), eq(10), any(CourseFiltersDto.class));
        }

        @Test
        void shouldReturnFilteredCourses() throws Exception {
            List<CourseDto> filteredCourses = List.of(courseDto);
            Page<CourseDto> filteredPage = new Page<>(filteredCourses, 1, 10, 1L);

            when(courseService.getAll(eq(1), eq(10), any(CourseFiltersDto.class))).thenReturn(filteredPage);

            mockMvc.perform(get("/api/speakly/courses")
                            .param("pageNumber", "1")
                            .param("pageSize", "10")
                            .param("language", "English")
                            .param("level", "A1")
                            .param("minPrice", "10")
                            .param("maxPrice", "30")
                            .param("sortBy", "priceAsc"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(1))
                    .andExpect(jsonPath("$.pageNumber").value(1))
                    .andExpect(jsonPath("$.pageSize").value(10))
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.data[0].id").value(filteredCourses.getFirst().id()));

            ArgumentCaptor<CourseFiltersDto> captor = ArgumentCaptor.forClass(CourseFiltersDto.class);
            verify(courseService).getAll(eq(1), eq(10), captor.capture());

            CourseFiltersDto usedFilters = captor.getValue();
            assertAll(
                    () -> org.junit.jupiter.api.Assertions.assertEquals("English", usedFilters.language()),
                    () -> org.junit.jupiter.api.Assertions.assertEquals("A1", usedFilters.level()),
                    () -> org.junit.jupiter.api.Assertions.assertEquals(10, usedFilters.minPrice()),
                    () -> org.junit.jupiter.api.Assertions.assertEquals(30, usedFilters.maxPrice()),
                    () -> org.junit.jupiter.api.Assertions.assertEquals("priceAsc", usedFilters.sortBy())
            );
        }

        @Test
        void shouldReturnCourseById() throws Exception {
            Mockito.when(courseService.getById(1L)).thenReturn(courseDto);

            mockMvc.perform(get("/api/speakly/courses/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("English A1"))
                    .andExpect(jsonPath("$.description").value("Basic English"))
                    .andExpect(jsonPath("$.price").value(19.99))
                    .andExpect(jsonPath("$.language").value("English"))
                    .andExpect(jsonPath("$.level").value("A1"))
                    .andExpect(jsonPath("$.teacher.id").value(10));
        }

        @Test
        void shouldCreateCourse() throws Exception {
            CourseInsertRequest request = new CourseInsertRequest(
                    "French A2",
                    "French basics",
                    new BigDecimal("25.00"),
                    "French",
                    "A2",
                    3L,
                    10,
                    LocalDateTime.now()
            );

            String newCourseJson = objectMapper.writeValueAsString(request);

            CourseDto createdCourse = new CourseDto(
                    1L,
                    "French A2",
                    "French basics",
                    new BigDecimal("25.00"),
                    "French",
                    "A2",
                    new UserDto(3L, null, null, null, null, null, List.of(), null),
                    10,
                    LocalDateTime.now()
            );

            Mockito.when(courseService.createCourse(Mockito.any()))
                    .thenReturn(createdCourse);

            mockMvc.perform(post("/api/speakly/courses")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newCourseJson))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.title").value("French A2"))
                    .andExpect(jsonPath("$.description").value("French basics"))
                    .andExpect(jsonPath("$.price").value(25.00))
                    .andExpect(jsonPath("$.language").value("French"))
                    .andExpect(jsonPath("$.level").value("A2"))
                    .andExpect(jsonPath("$.teacher.id").value(3L));

            verify(courseService).createCourse(any(CourseDto.class));
        }

        @Test
        void shouldUpdateCourse() throws Exception {
            CourseUpdateRequest request = new CourseUpdateRequest(
                    1L,
                    "German B2",
                    "Advanced German",
                    new BigDecimal("35.00"),
                    "German",
                    "B2",
                    7L,
                    15
            );

            CourseDto updatedCourse = new CourseDto(
                    1L,
                    request.title(),
                    request.description(),
                    request.price(),
                    request.language(),
                    request.level(),
                    new UserDto(request.teacherId(), null, null, null, null, null, List.of(), null),
                    request.duration(),
                    LocalDateTime.now()
            );

            Mockito.when(courseService.updateCourse(Mockito.any()))
                    .thenReturn(updatedCourse);

            mockMvc.perform(put("/api/speakly/courses/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("German B2"))
                    .andExpect(jsonPath("$.description").value("Advanced German"))
                    .andExpect(jsonPath("$.price").value(35.00))
                    .andExpect(jsonPath("$.language").value("German"))
                    .andExpect(jsonPath("$.level").value("B2"))
                    .andExpect(jsonPath("$.teacher.id").value(7));
        }

        @Test
        void shouldDeleteCourse() throws Exception {
            Mockito.doNothing().when(courseService).delete(1L);

            mockMvc.perform(delete("/api/speakly/courses/1"))
                    .andExpect(status().isNoContent());
        }


    }
}
