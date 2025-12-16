package es.speakly.store_backend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import es.speakly.store_backend.controller.webmodel.request.LoginRequest;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.service.AuthService;
import es.speakly.store_backend.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static es.speakly.store_backend.domain.model.UserRole.USER;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(userService);
    }

    @Test
    void login_ok_when_user_exists() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@email.com", "password");
        UserDto user = new UserDto(
                1L,
                "testuser",
                "test@email.com",
                null,
                "password",
                null,
                null,
                USER
        );

        when(userService.getByEmail("test@email.com")).thenReturn(user);
        when(authService.createTokenForUser(user)).thenReturn("jwt-token");

        mockMvc.perform(post("/api/speakly/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));

        }

    @Test
    void login_unauthorized_when_user_not_exists() throws Exception {
        LoginRequest loginRequest = new LoginRequest("noexiste@email.com", "password");

        when(userService.getByEmail("noexiste@email.com")).thenReturn(null);

        mockMvc.perform(post("/api/speakly/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_unauthorized_when_email_is_null() throws Exception {
        LoginRequest loginRequest = new LoginRequest(null, "password");

        when(userService.getByEmail(anyString())).thenReturn(null);

        mockMvc.perform(post("/api/speakly/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_ok() throws Exception {
        String token = "Bearer jwt-token";

        doNothing().when(authService).deleteToken(token);

        mockMvc.perform(post("/api/speakly/auth/logout")
                        .header("Authorization", token))
                .andExpect(status().isNoContent());

        verify(authService).deleteToken(token);
    }
}
