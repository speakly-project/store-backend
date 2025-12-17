package es.speakly.store_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.speakly.store_backend.annotations.AuthenticationInterceptor;
import es.speakly.store_backend.controller.webmodel.request.CourseInsertRequest;
import es.speakly.store_backend.controller.webmodel.request.CourseUpdateRequest;
import es.speakly.store_backend.domain.dto.CourseDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationInterceptor authenticationInterceptor;

    private CourseDto courseDto;

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
                10L,
                10,
                LocalDateTime.now()
        );
    }

    @BeforeEach
    void resetMocks() {
        Mockito.reset(courseService);
    }

    @Nested
    class GetTests {
        @Test
        void shouldReturnAllCourses() throws Exception {
            Page<CourseDto> page = new Page<>(
                    List.of(courseDto),
                    1,
                    10,
                    1L
            );

            Mockito.when(courseService.getAll(1, 10)).thenReturn(page);

            mockMvc.perform(get("/api/speakly/courses")
                            .param("pageNumber", "1")
                            .param("pageSize", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].title").value("English A1"))
                    .andExpect(jsonPath("$.data[0].description").value("Basic English"))
                    .andExpect(jsonPath("$.data[0].price").value(19.99))
                    .andExpect(jsonPath("$.data[0].language").value("English"))
                    .andExpect(jsonPath("$.data[0].level").value("A1"))
                    .andExpect(jsonPath("$.totalElements").value(1));
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
                    .andExpect(jsonPath("$.teacherId").value(10));
        }

        @Test
        void shouldReturnCoursesByLanguageAndLevel() throws Exception {
            Page<CourseDto> page = new Page<>(
                    List.of(courseDto),
                    1,
                    10,
                    1L
            );

            Mockito.when(courseService.getByLanguageAndLevel("English", "A1", 1, 10))
                    .thenReturn(page);

            mockMvc.perform(get("/api/speakly/courses/search")
                            .param("language", "English")
                            .param("level", "A1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].title").value("English A1"))
                    .andExpect(jsonPath("$.data[0].description").value("Basic English"))
                    .andExpect(jsonPath("$.data[0].price").value(19.99))
                    .andExpect(jsonPath("$.data[0].language").value("English"))
                    .andExpect(jsonPath("$.data[0].level").value("A1"))
                    .andExpect(jsonPath("$.totalElements").value(1));

        }

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
                3L,
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
                .andExpect(jsonPath("$.teacherId").value(3L));

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
                request.teacherId(),
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
                .andExpect(jsonPath("$.teacherId").value(7));
    }

    @Test
    void shouldDeleteCourse() throws Exception {
        Mockito.doNothing().when(courseService).delete(1L);

        mockMvc.perform(delete("/api/speakly/courses/1"))
                .andExpect(status().isNoContent());
    }


}
