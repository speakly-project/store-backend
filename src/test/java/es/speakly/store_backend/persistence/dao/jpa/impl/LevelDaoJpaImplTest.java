package es.speakly.store_backend.persistence.dao.jpa.impl;

import es.speakly.store_backend.persistence.dao.impl.LevelDaoJpaImpl;
import es.speakly.store_backend.persistence.dao.impl.entity.LevelJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(LevelDaoJpaImpl.class)
class LevelDaoJpaImplTest {

    @Autowired
    private LevelDaoJpaImpl levelDao;

    @PersistenceContext
    private EntityManager entityManager;

    private LevelJpaEntity level1;
    private LevelJpaEntity level2;

    @BeforeEach
    void setUp() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        level1 = new LevelJpaEntity(null, "TEST_A1_" + suffix);
        level2 = new LevelJpaEntity(null, "TEST_A2_" + suffix);

        entityManager.persist(level1);
        entityManager.persist(level2);
        entityManager.flush();
    }

    @Test
    void findById_notFound_returnsEmpty() {
        Optional<LevelJpaEntity> result = levelDao.findById(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByName_existing_returnsLevel() {
        Optional<LevelJpaEntity> result = levelDao.findByName(level1.getName());
        assertTrue(result.isPresent());
        assertEquals(level1.getName(), result.get().getName());
    }

    @Test
    void findByName_notFound_returnsEmpty() {
        Optional<LevelJpaEntity> result = levelDao.findByName("NO_EXISTE_" + UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void save_whenIdIsNull_shouldPersist() {
        LevelJpaEntity toSave = new LevelJpaEntity(null, "TEST_NEW_" + UUID.randomUUID().toString().substring(0, 8));
        LevelJpaEntity saved = levelDao.save(toSave);
        entityManager.flush();

        assertNotNull(saved.getId());
        assertEquals(toSave.getName(), saved.getName());
    }

    @Test
    void save_whenIdExists_shouldMerge() {
        level1.setName("TEST_UPDATED_" + UUID.randomUUID().toString().substring(0, 7));
        LevelJpaEntity updated = levelDao.save(level1);
        entityManager.flush();

        LevelJpaEntity fromDb = entityManager.find(LevelJpaEntity.class, level1.getId());
        assertEquals(updated.getName(), fromDb.getName());
    }

    @Test
    void findAll_shouldPaginateCorrectly() {
        List<LevelJpaEntity> page1 = levelDao.findAll(1, 2);
        assertNotNull(page1);
        assertTrue(page1.size() <= 2);
    }

    @Test
    void count_returnsCorrectValue() {
        long count = levelDao.count();
        assertTrue(count >= 2);
    }
}