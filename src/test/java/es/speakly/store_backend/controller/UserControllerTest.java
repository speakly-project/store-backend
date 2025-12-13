package es.speakly.store_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    // ==================== GET TESTS ====================

    @Test
    void findAllUsers() throws Exception {
        mockMvc.perform(get("/api/speakly/users")
                        .param("pageNumber", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").isNumber());
    }

    @Test
    void findAllUsersWithDefaultPagination() throws Exception {
        // Without params, should use defaults
        mockMvc.perform(get("/api/speakly/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(10));
    }

    @Test
    void findUserById() throws Exception {
        // Assuming seeded user with id 1 exists (see DB migrations V2)
        mockMvc.perform(get("/api/speakly/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.coursesTaken").isArray());
    }

    @Test
    void findUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/speakly/users/999999"))
                .andExpect(status().isNotFound());
    }

    // ==================== POST TESTS ====================

    @Test
    void createUser() throws Exception {
        String newUserJson = """
                {
                    "username": "test_user_new",
                    "email": "test_new@example.com",
                    "password": "secret123",
                    "profilePictureUrl": "https://example.com/avatar.png",
                    "createdAt": null,
                    "coursesTakenIds": []
                }
                """;

        mockMvc.perform(post("/api/speakly/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("test_user_new"))
                .andExpect(jsonPath("$.email").value("test_new@example.com"))
                .andExpect(jsonPath("$.profilePictureUrl").value("https://example.com/avatar.png"))
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.coursesTaken").isArray());
    }

    @Test
    void createUserWithCoursesTaken() throws Exception {
        // With ManyToMany relationship, users can enroll in existing courses
        String newUserJson = """
                {
                    "username": "test_user_with_courses",
                    "email": "test_courses@example.com",
                    "password": "password456",
                    "profilePictureUrl": "http://example.com/pic.png",
                    "createdAt": null,
                    "coursesTakenIds": [1,2,3]
                }
                """;

        mockMvc.perform(post("/api/speakly/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("test_user_with_courses"))
                .andExpect(jsonPath("$.email").value("test_courses@example.com"))
                .andExpect(jsonPath("$.coursesTaken").isArray())
                .andExpect(jsonPath("$.coursesTaken.length()").value(3));
    }

    @Test
    void createUserWithMissingUsername() throws Exception {
        String invalidUserJson = """
                {
                    "username": null,
                    "email": "test@example.com",
                    "password": "pass123",
                    "profilePictureUrl": null,
                    "createdAt": null,
                    "coursesTakenIds": []
                }
                """;

        mockMvc.perform(post("/api/speakly/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    void createUserWithEmptyBody() throws Exception {
        mockMvc.perform(post("/api/speakly/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}

