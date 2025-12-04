package es.speakly.store_backend.domain.model;



public class Language {
    
    private Long id;
    private String code;        // ej: "en", "es", "fr"
    private String name;        // ej: "English", "Spanish"

    public Language(Long id, String code, String name) {
        this.id = id;
        this.code = code;
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
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}