package es.speakly.store_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.speakly.store_backend.controller.webmodel.request.LevelInsertRequest;
import es.speakly.store_backend.domain.dto.LevelDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.service.LevelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LevelController.class)
public class LevelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LevelService levelService;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(levelService);
    }

    @Test
    void findAllLevels() throws Exception {
        List<LevelDto> levels = List.of(
                new LevelDto(1L, "A1"),
                new LevelDto(2L, "A2")
        );

        Page<LevelDto> levelPage = new Page<>(levels, 1, 100, levels.size());

        when(levelService.getAll(1, 100)).thenReturn(levelPage);

        mockMvc.perform(get("/api/speakly/levels")
                        .param("pageNumber", "1")
                        .param("pageSize", "100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(100))
                .andExpect(jsonPath("$.totalElements").value(2));

        verify(levelService).getAll(1, 100);
    }

    @Test
    void findAllLevelsWithDefaultPagination() throws Exception {
        List<LevelDto> levels = List.of(
                new LevelDto(1L, "A1"),
                new LevelDto(2L, "A2"),
                new LevelDto(3L, "B1")
        );

        Page<LevelDto> levelPage = new Page<>(levels, 1, 100, levels.size());

        when(levelService.getAll(1, 100)).thenReturn(levelPage);

        mockMvc.perform(get("/api/speakly/levels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(100))
                .andExpect(jsonPath("$.totalElements").value(3));

        verify(levelService).getAll(1, 100);
    }

    @Test
    void findLevelById() throws Exception {
        LevelDto level = new LevelDto(1L, "A1");

        when(levelService.getById(1L)).thenReturn(level);

        mockMvc.perform(get("/api/speakly/levels/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("A1"));

        verify(levelService).getById(1L);
    }

    @Test
    void getLevelsAmount() throws Exception {
        when(levelService.count()).thenReturn(9L);

        mockMvc.perform(get("/api/speakly/levels/amount"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").value(9));

        verify(levelService).count();
    }

    @Test
    void createLevel() throws Exception {
        LevelInsertRequest levelInsertRequest = new LevelInsertRequest("D1");

        LevelDto createdLevel = new LevelDto(10L, "D1");

        when(levelService.createLevel(any())).thenReturn(createdLevel);

        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(levelInsertRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("D1"));

        verify(levelService).createLevel(any(LevelDto.class));
    }

    @Test
    void createLevelWithMissingName() throws Exception {
        String invalidLevelJson = """
                {
                    "name": null
                }
                """;

        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLevelJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createLevelWithEmptyBody() throws Exception {
        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
