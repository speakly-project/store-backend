package es.speakly.store_backend.domain.model;

import es.speakly.store_backend.exceptions.ValidationException;

public class Language {
    
    private Long id;
    private String code;        // ISO 639-1 code: "en", "es", "fr"
    private String name;        // Full name: "English", "Spanish"

    public Language(Long id, String code, String name) {
        this.id = id;
        setCode(code);
        this.name = name;
    }

    public void validateCode() {
        if (code == null || code.trim().isEmpty()) {
            throw new ValidationException("Language code cannot be null or empty");
        }
        if (code.length() != 2) {
            throw new ValidationException("Language code must be exactly 2 characters (ISO 639-1 standard)");
        }
        if (!code.matches("^[a-z]{2}$")) {
            throw new ValidationException("Language code must contain only lowercase letters");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code.trim().toLowerCase();
        validateCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = (name != null) ? name.trim() : null;
        validateName();
    }

    public void validateName() {
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Language name cannot be null or empty");
        }
        if (name.length() < 2 || name.length() > 50) {
            throw new ValidationException("Language name must be between 2 and 50 characters");
        }
    }
}