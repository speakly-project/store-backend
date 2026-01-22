package es.speakly.store_backend.persistence.dao.jpa.impl;

import es.speakly.store_backend.domain.dto.CourseFiltersDto;
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
        entityManager.createQuery("DELETE FROM CourseJpaEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM UserJpaEntity").executeUpdate();
        entityManager.flush();
        entityManager.clear();

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
                () -> assertNotNull(result.get().getTeacher()),
                () -> assertEquals(teacher.getId(), result.get().getTeacher().getId())
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
    void findAll_withPagination_returnsCorrectPage() {
        List<CourseJpaEntity> page1 = courseDao.findAll(1, 2);
        List<CourseJpaEntity> page2 = courseDao.findAll(2, 2);

        assertAll(
                () -> assertNotNull(page1),
                () -> assertNotNull(page2),
                () -> assertEquals(2, page1.size()),
                () -> assertEquals(1, page2.size()), // Solo queda 1 elemento en página 2
                () -> assertNotEquals(page1.get(0).getId(), page2.get(0).getId())
        );
    }

    @Test
    void findAll_withPageNumberLessThan1_shouldReturnFirstPage() {
        List<CourseJpaEntity> page0 = courseDao.findAll(0, 10);
        List<CourseJpaEntity> page1 = courseDao.findAll(1, 10);

        assertAll(
                () -> assertNotNull(page0),
                () -> assertNotNull(page1),
                () -> assertEquals(page1.size(), page0.size()),
                () -> assertEquals(3, page0.size())
        );
    }

    @Test
    void findAllWithFilters_noFilters_returnsAll() {
        List<CourseJpaEntity> results = courseDao.findAllWithFilters(1, 10, null);

        assertAll(
                () -> assertNotNull(results),
                () -> assertEquals(3, results.size())
        );
    }

    @Test
    void findAllWithFilters_byLanguage_returnsFiltered() {
        CourseFiltersDto filters = new CourseFiltersDto("Spanish", null, null, null, null);
        List<CourseJpaEntity> results = courseDao.findAllWithFilters(1, 10, filters);

        assertAll(
                () -> assertNotNull(results),
                () -> assertEquals(2, results.size()),
                () -> assertTrue(results.stream().allMatch(c -> "Spanish".equals(c.getLanguage())))
        );
    }

    @Test
    void findAllWithFilters_byLevel_returnsFiltered() {
        CourseFiltersDto filters = new CourseFiltersDto(null, "A1", null, null, null);
        List<CourseJpaEntity> results = courseDao.findAllWithFilters(1, 10, filters);

        assertAll(
                () -> assertNotNull(results),
                () -> assertEquals(2, results.size()),
                () -> assertTrue(results.stream().allMatch(c -> "A1".equals(c.getLevel())))
        );
    }

    @Test
    void findAllWithFilters_byLanguageAndLevel_returnsFiltered() {
        CourseFiltersDto filters = new CourseFiltersDto("Spanish", "A1", null, null, null);
        List<CourseJpaEntity> results = courseDao.findAllWithFilters(1, 10, filters);

        assertAll(
                () -> assertNotNull(results),
                () -> assertEquals(2, results.size()),
                () -> assertTrue(results.stream().allMatch(c ->
                        "Spanish".equals(c.getLanguage()) && "A1".equals(c.getLevel())))
        );
    }

    @Test
    void findAllWithFilters_byMinPrice_returnsFiltered() {
        CourseFiltersDto filters = new CourseFiltersDto(null, null, 15, null, null);
        List<CourseJpaEntity> results = courseDao.findAllWithFilters(1, 10, filters);

        assertAll(
                () -> assertNotNull(results),
                () -> assertEquals(2, results.size()),
                () -> assertTrue(results.stream().allMatch(c ->
                        c.getPrice().compareTo(new BigDecimal("15")) >= 0))
        );
    }

    @Test
    void findAllWithFilters_byMaxPrice_returnsFiltered() {
        CourseFiltersDto filters = new CourseFiltersDto(null, null, null, 25, null);
        List<CourseJpaEntity> results = courseDao.findAllWithFilters(1, 10, filters);

        assertAll(
                () -> assertNotNull(results),
                () -> assertEquals(2, results.size()),
                () -> assertTrue(results.stream().allMatch(c ->
                        c.getPrice().compareTo(new BigDecimal("25")) <= 0))
        );
    }

    @Test
    void findAllWithFilters_byPriceRange_returnsFiltered() {
        CourseFiltersDto filters = new CourseFiltersDto(null, null, 15, 25, null);
        List<CourseJpaEntity> results = courseDao.findAllWithFilters(1, 10, filters);

        assertAll(
                () -> assertNotNull(results),
                () -> assertEquals(1, results.size()),
                () -> assertEquals(course2.getId(), results.get(0).getId())
        );
    }

    @Test
    void findAllWithFilters_combinedFilters_returnsFiltered() {
        CourseFiltersDto filters = new CourseFiltersDto("Spanish", "A1", 5, 25, null);
        List<CourseJpaEntity> results = courseDao.findAllWithFilters(1, 10, filters);

        assertAll(
                () -> assertNotNull(results),
                () -> assertEquals(2, results.size()),
                () -> assertTrue(results.stream().allMatch(c ->
                        "Spanish".equals(c.getLanguage()) &&
                                "A1".equals(c.getLevel()) &&
                                c.getPrice().compareTo(new BigDecimal("5")) >= 0 &&
                                c.getPrice().compareTo(new BigDecimal("25")) <= 0))
        );
    }

    @Test
    void findAllWithFilters_noMatchingResults_returnsEmpty() {
        CourseFiltersDto filters = new CourseFiltersDto("German", null, null, null, null);
        List<CourseJpaEntity> results = courseDao.findAllWithFilters(1, 10, filters);

        assertAll(
                () -> assertNotNull(results),
                () -> assertTrue(results.isEmpty())
        );
    }

    // ===============================
    // TESTS DE count y countWithFilters
    // ===============================

    @Test
    void count_returnsCorrectValue() {
        long total = courseDao.count();

        assertAll(
                () -> assertEquals(3L, total)
        );
    }

    @Test
    void countWithFilters_noFilters_returnsTotal() {
        long total = courseDao.countWithFilters(null);

        assertAll(
                () -> assertEquals(3L, total)
        );
    }

    @Test
    void countWithFilters_byLanguage_returnsFiltered() {
        CourseFiltersDto filters = new CourseFiltersDto("Spanish", null, null, null, null);
        long total = courseDao.countWithFilters(filters);

        assertAll(
                () -> assertEquals(2L, total)
        );
    }

    @Test
    void countWithFilters_byPriceRange_returnsFiltered() {
        CourseFiltersDto filters = new CourseFiltersDto(null, null, 15, 25, null);
        long total = courseDao.countWithFilters(filters);

        assertAll(
                () -> assertEquals(1L, total)
        );
    }

    @Test
    void countWithFilters_combinedFilters_returnsFiltered() {
        CourseFiltersDto filters = new CourseFiltersDto("Spanish", "A1", 5, 25, null);
        long total = courseDao.countWithFilters(filters);

        assertAll(
                () -> assertEquals(2L, total)
        );
    }

    @Test
    void countWithFilters_noMatchingResults_returnsZero() {
        CourseFiltersDto filters = new CourseFiltersDto("German", null, null, null, null);
        long total = courseDao.countWithFilters(filters);

        assertAll(
                () -> assertEquals(0L, total)
        );
    }

    // TESTS DE save (INSERT y UPDATE)


    @Test
    void save_newCourse_insertsSuccessfully() {
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

        CourseJpaEntity saved = courseDao.save(newCourse);
        entityManager.flush();
        entityManager.clear();

        CourseJpaEntity fromDb = entityManager.find(CourseJpaEntity.class, saved.getId());

        assertAll(
                () -> assertNotNull(saved.getId()),
                () -> assertNotNull(fromDb),
                () -> assertEquals("Course D", fromDb.getTitle()),
                () -> assertEquals(new BigDecimal("40.00"), fromDb.getPrice()),
                () -> assertEquals("German", fromDb.getLanguage()),
                () -> assertEquals("A2", fromDb.getLevel()),
                () -> assertNotNull(fromDb.getCreatedAt()),
                () -> assertNotNull(fromDb.getTeacher()),
                () -> assertEquals(teacher.getId(), fromDb.getTeacher().getId())
        );
    }

    @Test
    void save_existingCourse_updatesSuccessfully() {
        CourseJpaEntity existing = entityManager.find(CourseJpaEntity.class, course1.getId());
        existing.setTitle("Course A Updated");
        existing.setDuration(99);
        existing.setPrice(new BigDecimal("999.99"));

        CourseJpaEntity updated = courseDao.save(existing);
        entityManager.flush();
        entityManager.clear();

        CourseJpaEntity fromDb = entityManager.find(CourseJpaEntity.class, updated.getId());

        assertAll(
                () -> assertNotNull(updated),
                () -> assertEquals(existing.getId(), updated.getId()),
                () -> assertNotNull(fromDb),
                () -> assertEquals("Course A Updated", fromDb.getTitle()),
                () -> assertEquals(99, fromDb.getDuration()),
                () -> assertEquals(new BigDecimal("999.99"), fromDb.getPrice()),
                () -> assertNotNull(fromDb.getCreatedAt())
        );
    }

    @Test
    void deleteById_existingCourse_deletesSuccessfully() {
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
    void deleteById_nonExistingCourse_doesNotThrowException() {
        assertAll(
                () -> assertDoesNotThrow(() -> courseDao.deleteById(999999L))
        );
    }
}