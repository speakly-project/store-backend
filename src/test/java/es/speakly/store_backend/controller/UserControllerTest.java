package es.speakly.store_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.speakly.store_backend.annotations.AuthenticationInterceptor;
import es.speakly.store_backend.controller.webmodel.request.UserInsertRequest;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.service.UserService;
import es.speakly.store_backend.domain.usecase.PasswdUpdateUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static es.speakly.store_backend.domain.model.UserRole.USER;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswdUpdateUseCase passwdUpdateUseCase;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(userService, passwdUpdateUseCase);
    }

    @MockitoBean
    private AuthenticationInterceptor authenticationInterceptor;

    @BeforeEach
    void setUp() throws Exception {
        Mockito.when(authenticationInterceptor.preHandle(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(true);
    }


    @Test
    void findAllUsers() throws Exception {
        List<UserDto> users = List.of(
                new UserDto(1L, "user1", "user1@gmail.com", null, "hashedpassword1", null, null, USER),
                new UserDto(2L, "user2", "user2@gmail.com", null, "hashedpassword2", null, null, USER)
        );

        Page<UserDto> userPage = new Page<>(users, 1, 2, users.size());

        when(userService.getAll(1, 2)).thenReturn(userPage);

        mockMvc.perform(get("/api/speakly/users")
                        .param("pageNumber", "1")
                        .param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.totalElements").value(2));

        verify(userService).getAll(1, 2);
    }

    @Test
    void findAllUsersWithDefaultPagination() throws Exception {
        List<UserDto> users = List.of(
                new UserDto(1L, "user1", "user1@gmail.com", null, "hashedpassword1", null, null, USER),
                new UserDto(2L, "user2", "user2@gmail.com", null, "hashedpassword2", null, null, USER)
        );

        Page<UserDto> userPage = new Page<>(users, 1, 10, users.size());

        when(userService.getAll(1,10)).thenReturn(userPage);

        mockMvc.perform(get("/api/speakly/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.pageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(2));

        verify(userService).getAll(1, 10);
    }

    @Test
    void findUserById() throws Exception {
        UserDto user = new UserDto(
                1L,
                "test_user",
                "usesrdto@gmail.com",
                null,
                "hashedpassword",
                LocalDateTime.now(),
                null,
                USER
        );

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String createdAtString = user.createdAt().format(formatter);

        when(userService.getById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/speakly/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("test_user"))
                .andExpect(jsonPath("$.email").value("usesrdto@gmail.com"))
                .andExpect(jsonPath("$.createdAt").value(createdAtString))
                .andExpect(jsonPath("$.coursesTaken").isArray());
    }

    @Test
    void createUser() throws Exception {
        UserInsertRequest userInsertRequest = new UserInsertRequest(
                "test_user_new",
                "test_new@example.com",
                "https://example.com/avatar.png",
                "secret123",
                null,
                null,
                USER
        );

        String newUserJson = objectMapper.writeValueAsString(userInsertRequest);

        UserDto createdUser = new UserDto(
                1L,
                "test_user_new",
                "test_new@example.com",
                "https://example.com/avatar.png",
                "secret123",
                LocalDateTime.now(),
                null,
                USER
        );

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String createdAtString = createdUser.createdAt().format(formatter);

        when(userService.createUser(any())).thenReturn(createdUser);

        mockMvc.perform(post("/api/speakly/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("test_user_new"))
                .andExpect(jsonPath("$.email").value("test_new@example.com"))
                .andExpect(jsonPath("$.profilePictureUrl").value("https://example.com/avatar.png"))
                .andExpect(jsonPath("$.createdAt").value(createdAtString))
                .andExpect(jsonPath("$.coursesTaken").isArray());

        verify(userService).createUser(any(UserDto.class));
    }


    @Test
    void createUserWithCoursesTaken() throws Exception {
        UserInsertRequest userInsertRequest = new UserInsertRequest(
                "test_user_with_courses",
                "test_courses@example.com",
                "http://example.com/pic.png",
                "password456",
                null,
                List.of(1L, 2L, 3L).toArray(Long[]::new),
                USER
        );

        objectMapper.registerModule(new JavaTimeModule());
        String newUserJson = objectMapper.writeValueAsString(userInsertRequest);

        UserDto createdUser = new UserDto(
                1L,
                "test_user_with_courses",
                "test_courses@example.com",
                "http://example.com/pic.png",
                "password456",
                LocalDateTime.now(),
                Stream.of(1L, 2L, 3L)
                        .map(id -> new es.speakly.store_backend.domain.dto.CourseDto(id, "Course " + id, null, null, null, null, null, 0, null))
                        .toList(),
                USER
        );

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String createdAtString = createdUser.createdAt().format(formatter);

        when(userService.createUser(any())).thenReturn(createdUser);

        mockMvc.perform(post("/api/speakly/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("test_user_with_courses"))
                .andExpect(jsonPath("$.email").value("test_courses@example.com"))
                .andExpect(jsonPath("$.coursesTaken").isArray())
                .andExpect(jsonPath("$.coursesTaken.length()").value(3));

        verify(userService).createUser(any(UserDto.class));

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

    @Test
    void updateUserAdmin_shouldReturnOkAndUpdatedUser() throws Exception {
        UserDto updatedUser = new UserDto(
                1L,
                "new_username",
                "new_username@example.es",
                null,
                "ignored",
                LocalDateTime.now(),
                List.of(),
                USER
        );

        String requestJson = """
                {
                  "id": 1,
                  "username": "new_username",
                  "email": "new_username@example.es",
                  "profilePictureUrl": null,
                  "password": null,
                  "createAt": null,
                  "coursesIds": [],
                  "role": "USER"
                }
                """;

        when(userService.updateUser(any(UserDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/speakly/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("new_username"))
                .andExpect(jsonPath("$.email").value("new_username@example.es"))
                .andExpect(jsonPath("$.coursesTaken").isArray());

        verify(userService).updateUser(any(UserDto.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void updateMyUser_shouldReturnOkAndNotAdminResponse() throws Exception {
        UserDto updatedUser = new UserDto(
                5L,
                "me_new",
                "me_new@example.es",
                "http://pic",
                "ignored",
                LocalDateTime.now(),
                List.of(),
                USER
        );

        String requestJson = """
                {
                  "id": 5,
                  "username": "me_new",
                  "email": "me_new@example.es",
                  "profilePictureUrl": "http://pic",
                  "password": null,
                  "createAt": null,
                  "coursesIds": [],
                  "role": "USER"
                }
                """;

        when(userService.updateUser(any(UserDto.class))).thenReturn(updatedUser);

        var result = mockMvc.perform(put("/api/speakly/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.username").value("me_new"))
                .andExpect(jsonPath("$.email").value("me_new@example.es"))
                .andExpect(jsonPath("$.profilePictureUrl").value("http://pic"))
                .andReturn();

        verify(userService).updateUser(any(UserDto.class));
        verifyNoMoreInteractions(userService);
    }
}
