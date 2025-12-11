package es.speakly.store_backend.persistence.dao.impl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="languages")
public class LanguageJpaEntity {
    @Id
    private Long id;
    private String name;
    private String code;

    public LanguageJpaEntity() {
    }

    public LanguageJpaEntity(Long id, String code, String name) {
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

    public String getName() {
        return name;
    }
}
