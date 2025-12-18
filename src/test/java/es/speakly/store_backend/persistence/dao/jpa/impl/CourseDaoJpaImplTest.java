package es.speakly.store_backend.persistence.dao.jpa.impl;

import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.persistence.dao.impl.CourseDaoJpaImpl;
import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(CourseDaoJpaImpl.class)
public class CourseDaoJpaImplTest {

    @Autowired
    private CourseDaoJpaImpl courseDao;

    @PersistenceContext
    private EntityManager entityManager;

    private UserJpaEntity teacher;
    private CourseJpaEntity course1;
    private CourseJpaEntity course2;
    private CourseJpaEntity course3;

    @BeforeEach
    void setUp() {
        teacher = new UserJpaEntity(
                null,
                "teacher1",
                "teacher1@email.com",
                null,
                "encrypted-password",
                LocalDateTime.now(),
                null,
                UserRole.USER
        );
        entityManager.persist(teacher);
        entityManager.flush();

        LocalDateTime now = LocalDateTime.now();

        course1 = new CourseJpaEntity(
                null,
                "Course A",
                "Desc A",
                new BigDecimal("10.00"),
                "Spanish",
                "A1",
                10,
                teacher
        );
        course1.setCreatedAt(now);

        course2 = new CourseJpaEntity(
                null,
                "Course B",
                "Desc B",
                new BigDecimal("20.00"),
                "Spanish",
                "A1",
                12,
                teacher
        );
        course2.setCreatedAt(now.plusSeconds(1));

        course3 = new CourseJpaEntity(
                null,
                "Course C",
                "Desc C",
                new BigDecimal("30.00"),
                "French",
                "B1",
                8,
                teacher
        );
        course3.setCreatedAt(now.plusSeconds(2));

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.persist(course3);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findById_existingCourse_returnsCourse() {
        Optional<CourseJpaEntity> result = courseDao.findById(course1.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(course1.getId(), result.get().getId()),
                () -> assertEquals(course1.getTitle(), result.get().getTitle()),
                () -> assertEquals(course1.getLanguage(), result.get().getLanguage()),
                () -> assertEquals(course1.getLevel(), result.get().getLevel()),
                () -> assertEquals(course1.getDuration(), result.get().getDuration()),
                () -> assertNotNull(result.get().getCreatedAt()),
                () -> assertNotNull(result.get().getUser()),
                () -> assertEquals(teacher.getId(), result.get().getUser().getId())
        );
    }

    @Test
    void findById_notFound_returnsEmpty() {
        Optional<CourseJpaEntity> result = courseDao.findById(999999L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void findByTitle_existing_returnsCourse() {
        Optional<CourseJpaEntity> result = courseDao.findByTitle("Course B");

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(course2.getId(), result.get().getId()),
                () -> assertEquals("Course B", result.get().getTitle()),
                () -> assertNotNull(result.get().getCreatedAt())
        );
    }

    @Test
    void findByTitle_notFound_returnsEmpty() {
        Optional<CourseJpaEntity> result = courseDao.findByTitle("does-not-exist");

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void findByLanguageAndLevel_shouldReturnPage() {
        List<CourseJpaEntity> page1 = courseDao.findByLanguageAndLevel("Spanish", "A1", 1, 1);
        List<CourseJpaEntity> page2 = courseDao.findByLanguageAndLevel("Spanish", "A1", 2, 1);

        assertAll(
                () -> assertNotNull(page1),
                () -> assertNotNull(page2),
                () -> assertEquals(1, page1.size()),
                () -> assertEquals(1, page2.size()),
                () -> assertEquals("Spanish", page1.getFirst().getLanguage()),
                () -> assertEquals("A1", page1.getFirst().getLevel()),
                () -> assertEquals("Spanish", page2.getFirst().getLanguage()),
                () -> assertEquals("A1", page2.getFirst().getLevel()),
                () -> assertNotEquals(page1.getFirst().getId(), page2.getFirst().getId())
        );
    }

    @Test
    void findAll_withPageNumberLessThan1_shouldReturn1() {
        List<CourseJpaEntity> page0 = courseDao.findByLanguageAndLevel("Spanish", "A1", 0, 10);
        List<CourseJpaEntity> page1 = courseDao.findByLanguageAndLevel("Spanish", "A1", 1, 10);


        assertAll(
                () -> assertNotNull(page0),
                () -> assertNotNull(page1),
                () -> assertEquals(page1.size(), page0.size())
        );
    }

    @Test
    void insert_validCourse_success() {
        CourseJpaEntity newCourse = new CourseJpaEntity(
                null,
                "Course D",
                "Desc D",
                new BigDecimal("40.00"),
                "German",
                "A2",
                9,
                teacher
        );
        newCourse.setCreatedAt(LocalDateTime.now());

        CourseJpaEntity saved = courseDao.insert(newCourse);
        entityManager.flush();
        entityManager.clear();

        CourseJpaEntity fromDb = entityManager.find(CourseJpaEntity.class, saved.getId());

        assertAll(
                () -> assertNotNull(saved.getId()),
                () -> assertNotNull(fromDb),
                () -> assertEquals("Course D", fromDb.getTitle()),
                () -> assertNotNull(fromDb.getCreatedAt()),
                () -> assertNotNull(fromDb.getUser()),
                () -> assertEquals(teacher.getId(), fromDb.getUser().getId())
        );
    }

    @Test
    void update_existingCourse_success() {
        CourseJpaEntity existing = entityManager.find(CourseJpaEntity.class, course1.getId());
        existing.setTitle("Course A Updated");
        existing.setDuration(99);

        CourseJpaEntity updated = courseDao.update(existing);
        entityManager.flush();
        entityManager.clear();

        CourseJpaEntity fromDb = entityManager.find(CourseJpaEntity.class, updated.getId());

        assertAll(
                () -> assertNotNull(updated),
                () -> assertEquals(existing.getId(), updated.getId()),
                () -> assertNotNull(fromDb),
                () -> assertEquals("Course A Updated", fromDb.getTitle()),
                () -> assertEquals(99, fromDb.getDuration()),
                () -> assertNotNull(fromDb.getCreatedAt())
        );
    }

    @Test
    void update_notFound_throwsException() {
        CourseJpaEntity fake = new CourseJpaEntity(
                999999L,
                "Fake",
                "Fake",
                new BigDecimal("1.00"),
                "Spanish",
                "A1",
                1,
                teacher
        );
        fake.setCreatedAt(LocalDateTime.now());

        assertAll(
                () -> assertThrows(RuntimeException.class, () -> courseDao.update(fake))
        );
    }

    @Test
    void deleteById_existingCourse_deletes() {
        Long id = course3.getId();

        courseDao.deleteById(id);
        entityManager.flush();
        entityManager.clear();

        CourseJpaEntity deleted = entityManager.find(CourseJpaEntity.class, id);

        assertAll(
                () -> assertNull(deleted)
        );
    }

    @Test
    void count_returnsCorrectValue() {
        long total = courseDao.count();

        assertAll(
                () -> assertEquals(73L, total)
        );
    }
}