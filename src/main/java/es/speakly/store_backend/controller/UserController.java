package es.speakly.store_backend.controller;


import es.speakly.store_backend.annotations.Admin;
import es.speakly.store_backend.annotations.Authenticated;
import es.speakly.store_backend.controller.webmodel.request.UserInsertRequest;
import es.speakly.store_backend.controller.webmodel.request.UserUpdateNotAdminRequest;
import es.speakly.store_backend.controller.webmodel.request.UserUpdateRequest;
import es.speakly.store_backend.controller.webmodel.response.UserDetailNotAdminResponse;
import es.speakly.store_backend.controller.webmodel.response.UserDetailResponse;
import es.speakly.store_backend.controller.webmodel.response.UserDetailResponse;
import es.speakly.store_backend.domain.dto.UserDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.service.UserService;
import es.speakly.store_backend.exceptions.DtoValidator;
import es.speakly.store_backend.mappers.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/speakly/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDetailResponse>> findAllUsers(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                                                  @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Page<UserDto> usersDtoPage = userService.getAll(pageNumber, pageSize);

        List<UserDetailResponse> userDetailResponses = usersDtoPage.data().stream()
                .map(UserMapper::fromUserDtoToUserDetailResponse).toList();

        Page<UserDetailResponse> userSummaryResponsePage = new Page<>(
                userDetailResponses,
                usersDtoPage.pageNumber(),
                usersDtoPage.pageSize(),
                usersDtoPage.totalElements()
        );
        return new ResponseEntity<>(userSummaryResponsePage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> findUserById(@PathVariable Long id) {
        UserDetailResponse userDetailResponse = UserMapper.fromUserDtoToUserDetailResponse(userService.getById(id));
        return new ResponseEntity<>(userDetailResponse, HttpStatus.OK);
    }

    @GetMapping("/username")
    public ResponseEntity<UserDetailResponse> findUserByUsername(@RequestParam String username) {
        UserDetailResponse userDetailResponse = UserMapper.fromUserDtoToUserDetailResponse(userService.getByUsername(username));
        return new ResponseEntity<>(userDetailResponse, HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<UserDetailResponse> findUserByEmail(@RequestParam String email) {
        UserDetailResponse userDetailResponse = UserMapper.fromUserDtoToUserDetailResponse(userService.getByEmail(email));
        return new ResponseEntity<>(userDetailResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDetailResponse> createUser(@RequestBody UserInsertRequest userInsertRequest) {
        UserDto userDto = UserMapper.fromUserInsertRequestToUserDto(userInsertRequest);
        DtoValidator.validate(userDto);
        UserDto createdUserDto = userService.createUser(userDto);
        UserDetailResponse userDetailResponse = UserMapper.fromUserDtoToUserDetailResponse(createdUserDto);
        return new ResponseEntity<>(userDetailResponse, HttpStatus.CREATED);
    }

    @Admin
    @PutMapping("/{id}")
    public ResponseEntity<UserDetailResponse> updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateRequest userUpdateRequest){
        if (!id.equals(userUpdateRequest.id())){
            throw new IllegalArgumentException("ID in path and request body must match");
        }
        UserDto userDto = UserMapper.fromUserUpdateRequestToUserDto(userUpdateRequest);
        DtoValidator.validate(userDto);
        UserDto updatedUserDto = userService.updateUser(userDto);
        UserDetailResponse userDetailResponse = UserMapper.fromUserDtoToUserDetailResponse(updatedUserDto);
        return new ResponseEntity<>(userDetailResponse, HttpStatus.OK);
    }

    @Authenticated
    @PutMapping("/me")
    public ResponseEntity<UserDetailNotAdminResponse> updateUser(@RequestBody UserUpdateNotAdminRequest userUpdateNotAdmin){
        UserDto userDto = UserMapper.fromUserUpdateNotAdminRequestToUserDto(userUpdateNotAdmin);
        DtoValidator.validate(userDto);
        UserDto updatedUserDto = userService.updateUser(userDto);
        UserDetailNotAdminResponse userDetailResponse = UserMapper.fromUserDtoToUserDetailNotAdminResponse(updatedUserDto);
        return new ResponseEntity<>(userDetailResponse, HttpStatus.OK);
    }

    @Admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
