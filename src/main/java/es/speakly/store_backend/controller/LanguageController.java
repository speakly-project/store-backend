package es.speakly.store_backend.controller;

import es.speakly.store_backend.domain.dto.LanguageDto;
import es.speakly.store_backend.domain.model.Page;
import es.speakly.store_backend.domain.service.LanguageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/speakly/languages")
public class LanguageController {
    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public ResponseEntity<Page<LanguageDto>> findAllLanguages(
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "20") int pageSize) {
        Page<LanguageDto> languagesDtoPage = languageService.getAll(pageNumber, pageSize);
        return new ResponseEntity<>(languagesDtoPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageDto> findLanguageById(@PathVariable Long id) {
        LanguageDto languageDto = languageService.getById(id);
        return new ResponseEntity<>(languageDto, HttpStatus.OK);
    }

    @GetMapping("/amount")
    public ResponseEntity<Long> getLanguagesAmount() {
        long amount = languageService.count();
        return new ResponseEntity<>(amount, HttpStatus.OK);
    }

}
