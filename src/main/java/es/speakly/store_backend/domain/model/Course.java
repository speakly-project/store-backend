package es.speakly.store_backend.domain.model;

import lombok.Getter;
import java.util.UUID;

@Getter
public class Course {
    
    private final UUID id;
    private final String title;
    private final String description;
    private final float price;
    private final UUID languageId;
    private final String level;
    private final UUID teacherId;
    
    public Course(String title, String description, float price, UUID languageId, String level, UUID teacherId) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.price = price;
        this.languageId = languageId;
        this.level = level;
        this.teacherId = teacherId;
    }
    
    public Course(UUID id, String title, String description, Float price, 
                  UUID languageId, String level, UUID teacherId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.languageId = languageId;
        this.level = level;
        this.teacherId = teacherId;
    }
    
    public boolean isOwnedBy(UUID userId) {
        return this.teacherId.equals(userId);
    }
    
    public boolean isInLanguage(UUID languageId) {
        return this.languageId.equals(languageId);
    }
}