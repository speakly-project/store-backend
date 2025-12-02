package es.speakly.store_backend.domain.model;
import lombok.Getter;
import java.util.UUID;

@Getter
public class Language {
    
    private final UUID id;
    private final String code;        // ej: "en", "es", "fr"
    private final String name;        // ej: "English", "Spanish"

    public Language(String code, String name) {
        this.id = UUID.randomUUID();
        this.code = code;
        this.name = name;
    }
    
    public Language(UUID id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}