package es.speakly.store_backend.controller;


import es.speakly.store_backend.controller.webmodel.request.LoginRequest;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.service.AuthService;
import es.speakly.store_backend.domain.service.UserService;
import es.speakly.store_backend.exceptions.DtoValidator;
import es.speakly.store_backend.persistence.dao.AuthDao;
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

        if (userService.getByEmail(loginRequest.email()) == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authService.createTokenForUser(userService.getByEmail(loginRequest.email()));

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        authService.deleteToken(token);
        return new ResponseEntity<>("Logout successful", HttpStatus.NO_CONTENT);
    }
}
