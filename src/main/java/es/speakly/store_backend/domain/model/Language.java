package es.speakly.store_backend.domain.model;

import es.speakly.store_backend.exceptions.ValidationException;

public class Language {
    
    private Long id;
    private String name;        // Full name: "English", "Spanish"
    private String code;        // ISO 639-1 code: "en", "es", "fr"

    public Language(Long id, String code, String name) {
        this.id = id;
        setCode(code);
        this.name = name;
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
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = (name != null) ? name.trim() : null;
    }

}