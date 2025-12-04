package es.speakly.store_backend.controller.webmodel.request;


import java.time.LocalDateTime;

public record UserUpdateRequest(
        Long id,
        String username,
        String email,
        String profilePictureUrl,
        String password,
        LocalDateTime createAt,
        Long[] coursesIds

) {

}
