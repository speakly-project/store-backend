package es.speakly.store_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.speakly.store_backend.controller.webmodel.request.LevelInsertRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LevelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    // ...existing GET tests...

    @Test
    void findAllLevels() throws Exception {
        mockMvc.perform(get("/api/speakly/levels")
                        .param("pageNumber", "1")
                        .param("pageSize", "100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(100))
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath("$.data[0].name").exists());
    }

    @Test
    void findLevelById() throws Exception {
        mockMvc.perform(get("/api/speakly/levels/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("A1"));
    }

    @Test
    void findLevelByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/speakly/levels/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getLevelsAmount() throws Exception {
        mockMvc.perform(get("/api/speakly/levels/amount"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void verifyAllCEFRLevels() throws Exception {
        String[] cefrLevels = {"A1", "A2", "B1", "B2", "C1", "C2"};
        for (int i = 0; i < cefrLevels.length; i++) {
            mockMvc.perform(get("/api/speakly/levels/" + (i + 1)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(cefrLevels[i]));
        }
    }
    @Test
    void verifyCustomLevels() throws Exception {
        // Verify custom English levels
        mockMvc.perform(get("/api/speakly/levels/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("BASIC"));

        mockMvc.perform(get("/api/speakly/levels/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("INTERMEDIATE"));

        mockMvc.perform(get("/api/speakly/levels/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ADVANCED"));
    }

    // ==================== POST TESTS (CRITICAL CASES) ====================

    @Test
    void createLevel_Success() throws Exception {
        // Arrange - Create a unique level name
        LevelInsertRequest newLevel = new LevelInsertRequest("D1");

        // Act & Assert
        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newLevel)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(newLevel.name()));
    }

    @Test
    void createLevel_DuplicateName_ShouldFail() throws Exception {
        // Arrange - Try to create a level with existing name "A1"
        LevelInsertRequest duplicateLevel = new LevelInsertRequest("A1");

        // Act & Assert - Should return 400 Bad Request
        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateLevel)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createLevel_NullName_ShouldFail() throws Exception {
        // Arrange - Try to create a level with null name
        String invalidJson = "{\"name\": null}";

        // Act & Assert - Should return 400 Bad Request
        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createLevel_EmptyName_ShouldFail() throws Exception {
        // Arrange - Try to create a level with empty name
        LevelInsertRequest emptyNameLevel = new LevelInsertRequest("");

        // Act & Assert - Should return 400 Bad Request
        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyNameLevel)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createLevel_NameTooLong_ShouldFail() throws Exception {
        // Arrange - Try to create a level with name longer than 20 characters
        LevelInsertRequest tooLongNameLevel = new LevelInsertRequest("THIS_NAME_IS_WAY_TOO_LONG_TO_BE_VALID");

        // Act & Assert - Should return 400 Bad Request
        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tooLongNameLevel)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createLevel_WhitespaceOnlyName_ShouldFail() throws Exception {
        // Arrange - Try to create a level with only whitespace
        LevelInsertRequest whitespaceLevel = new LevelInsertRequest("   ");

        // Act & Assert - Should return 400 Bad Request
        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(whitespaceLevel)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createLevel_ValidNameWithSpaces_Success() throws Exception {
        // Arrange - Create a level with valid name containing spaces
        LevelInsertRequest levelWithSpaces = new LevelInsertRequest("TEST LEVEL");

        // Act & Assert
        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(levelWithSpaces)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(levelWithSpaces.name()));
    }

    @Test
    void createLevel_SpecialCharacters_Success() throws Exception {
        // Arrange - Create a level with special characters
        LevelInsertRequest specialCharLevel = new LevelInsertRequest("A1+");

        // Act & Assert
        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(specialCharLevel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(specialCharLevel.name()));
    }

    @Test
    void createMultipleLevels_Sequential_Success() throws Exception {
        // Arrange - Create multiple levels in sequence
        LevelInsertRequest level1 = new LevelInsertRequest("SEQ1");
        LevelInsertRequest level2 = new LevelInsertRequest("SEQ2");

        // Act & Assert - Both should succeed
        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(level1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(level1.name()));

        mockMvc.perform(post("/api/speakly/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(level2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(level2.name()));
    }
}


