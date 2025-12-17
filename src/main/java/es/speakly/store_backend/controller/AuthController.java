package es.speakly.store_backend.controller;


import es.speakly.store_backend.annotations.Admin;
import es.speakly.store_backend.annotations.Authenticated;
import es.speakly.store_backend.controller.webmodel.request.LoginRequest;
import es.speakly.store_backend.controller.webmodel.response.UserDetailResponse;
import es.speakly.store_backend.domain.dto.LoginUserDto;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.service.AuthService;
import es.speakly.store_backend.domain.service.UserService;
import es.speakly.store_backend.exceptions.DtoValidator;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/speakly/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;


    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        DtoValidator.validate(loginRequest);
        UserDto user = userService.getByEmail(loginRequest.email());

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDto authenticatedUser = new UserDto(
                user.id(),
                user.username(),
                user.email(),
                user.profilePictureUrl(),
                loginRequest.password(),
                user.createdAt(),
                user.coursesTaken(),
                user.role()
        );
        String token = authService.createTokenForUser(authenticatedUser);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @Authenticated
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        authService.deleteToken(token);
        return new ResponseEntity<>("Logout successful", HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<LoginUserDto> getUserFromToken(@RequestHeader("Authorization") String token) {
        LoginUserDto loggedUser = authService.getUserFromToken(token.substring(7));
        return new ResponseEntity<>(loggedUser, HttpStatus.OK);
    }
}
