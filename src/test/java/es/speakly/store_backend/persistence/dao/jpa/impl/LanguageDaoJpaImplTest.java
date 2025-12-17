package es.speakly.store_backend.persistence.dao.jpa.impl;

import es.speakly.store_backend.persistence.dao.impl.LanguageDaoJpaImpl;
import es.speakly.store_backend.persistence.dao.impl.entity.LanguageJpaEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(LanguageDaoJpaImpl.class)
class LanguageDaoJpaImplTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LanguageDaoJpaImpl languageDao;

    @BeforeEach
    void setUp() {
        entityManager.createQuery("DELETE FROM LanguageJpaEntity").executeUpdate();

        entityManager.persist(new LanguageJpaEntity(101L, "en", "English"));
        entityManager.persist(new LanguageJpaEntity(102L, "es", "Spanish"));
        entityManager.persist(new LanguageJpaEntity(103L, "fr", "French"));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findAll_debePaginarCorrectamente() {
        List<LanguageJpaEntity> page1 = languageDao.findAll(1, 2);
        List<LanguageJpaEntity> page2 = languageDao.findAll(2, 2);

        assertAll(
                () -> assertNotNull(page1),
                () -> assertEquals(2, page1.size()),
                () -> assertNotNull(page2),
                () -> assertEquals(1, page2.size()),
                () -> assertEquals(103L, page2.get(0).getId())
        );
    }

    @Test
    void findAll_conPageNumberMenorQue1_debeTratarloComoPrimeraPagina() {
        List<LanguageJpaEntity> result = languageDao.findAll(0, 1);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(101L, result.get(0).getId())
        );
    }

    @Test
    void findById_cuandoExiste_debeDevolverEntidad() {
        entityManager.persist(new LanguageJpaEntity(110L, "de", "German"));
        entityManager.flush();
        entityManager.clear();

        Optional<LanguageJpaEntity> result = languageDao.findById(110L);

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(110L, result.get().getId()),
                () -> assertEquals("de", result.get().getCode()),
                () -> assertEquals("German", result.get().getName())
        );
    }

    @Test
    void findById_cuandoNoExiste_debeDevolverEmpty() {
        Optional<LanguageJpaEntity> result = languageDao.findById(999999L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void count_debeDevolverNumeroTotal() {
        long total = languageDao.count();
        assertEquals(3L, total);
    }
}