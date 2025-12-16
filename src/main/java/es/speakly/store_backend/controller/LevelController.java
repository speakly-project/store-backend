package es.speakly.store_backend.controller;

import es.speakly.store_backend.annotations.Admin;
import es.speakly.store_backend.annotations.Authenticated;
import es.speakly.store_backend.controller.webmodel.request.LevelInsertRequest;
import es.speakly.store_backend.domain.dto.LevelDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.service.LevelService;
import es.speakly.store_backend.exceptions.DtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/speakly/levels")
public class LevelController {
    private final LevelService levelService;

    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @GetMapping
    public ResponseEntity<Page<LevelDto>> findAllLevels(
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "100") int pageSize) {
        Page<LevelDto> levelsDtoPage = levelService.getAll(pageNumber, pageSize);
        return new ResponseEntity<>(levelsDtoPage, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LevelDto> findLevelById(@PathVariable Long id) {
        LevelDto levelDto = levelService.getById(id);
        return new ResponseEntity<>(levelDto, HttpStatus.OK);
    }
//@GetMapping("/{id}")
//public ResponseEntity<LevelDto> findLevelById(
//        @PathVariable Long id,
//        HttpServletRequest request
//) {
//    LoginUserDto user = (LoginUserDto) request.getAttribute("user");
//    Long userId = user.id();
//
//    if (userId == null) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
//
//    LevelDto levelDto = levelService.getById(id);
//    return new ResponseEntity<>(levelDto, HttpStatus.OK);
//}

    @GetMapping("/amount")
    public ResponseEntity<Long> getLevelsAmount() {
        long amount = levelService.count();
        return new ResponseEntity<>(amount, HttpStatus.OK);
    }

    @Admin
    @PostMapping
    public ResponseEntity<LevelDto> createLevel(@RequestBody LevelInsertRequest levelInsertRequest) {
        LevelDto levelDto = new LevelDto(null, levelInsertRequest.name());
        DtoValidator.validate(levelDto);
        LevelDto createdLevel = levelService.createLevel(levelDto);

        return new ResponseEntity<>(createdLevel, HttpStatus.CREATED);
    }
}

