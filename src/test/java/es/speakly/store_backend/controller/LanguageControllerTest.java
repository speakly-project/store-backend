package es.speakly.store_backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LanguageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllLanguages() throws Exception {
        mockMvc.perform(get("/api/speakly/languages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").isNumber());
    }

    @Test
    void findLanguageById() throws Exception {
        // Assuming there's a language with id=1 in the database (from Flyway migrations)
        mockMvc.perform(get("/api/speakly/languages/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.code").exists());
    }

    @Test
    void findLanguageByIdNotFound() throws Exception {
        // Test with non-existent id
        mockMvc.perform(get("/api/speakly/languages/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getLanguagesAmount() throws Exception {
        // Act & Assert - verifying the count endpoint returns a number
        mockMvc.perform(get("/api/speakly/languages/amount"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isNumber())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.greaterThanOrEqualTo(0)));
    }
}