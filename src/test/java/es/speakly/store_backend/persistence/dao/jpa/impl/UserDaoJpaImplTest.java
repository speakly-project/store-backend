package es.speakly.store_backend.persistence.dao.jpa.impl;


import es.speakly.store_backend.domain.model.UserRole;
import es.speakly.store_backend.persistence.dao.impl.UserDaoJpaImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = "spring.flyway.enabled=false")
@Import(UserDaoJpaImpl.class)
public class UserDaoJpaImplTest {
    @Autowired
    private UserDaoJpaImpl userDao;

    @PersistenceContext
    private EntityManager entityManager;

    private UserJpaEntity user1;
    private UserJpaEntity user2;

    @BeforeEach
    void setUp() {
        entityManager.createQuery("DELETE FROM SessionJpaEntity").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM user_courses").executeUpdate();
        entityManager.createQuery("DELETE FROM UserJpaEntity").executeUpdate();
        entityManager.flush();
        entityManager.clear();

        user1 = new UserJpaEntity(
                null,
                "testuser",
                "test@email.com",
                null,
                "encrypted-password",
                LocalDateTime.now(),
                null,
                UserRole.USER
        );

        user2 = new UserJpaEntity(
                null,
                "admin",
                "admin@email.com",
                null,
                "encrypted-password",
                LocalDateTime.now(),
                null,
                UserRole.ADMIN
        );

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findById_existingUser_returnsUser() {
        Optional<UserJpaEntity> result = userDao.findById(user1.getId());

        assertTrue(result.isPresent());
        assertEquals(user1.getUsername(), result.get().getUsername());
    }

    @Test
    void findById_notFound_returnsEmpty() {
        Optional<UserJpaEntity> result = userDao.findById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUsername_existing_returnsUser() {
        Optional<UserJpaEntity> result = userDao.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals(user1.getEmail(), result.get().getEmail());
    }

    @Test
    void findByUsername_notFound_returnsEmpty() {
        Optional<UserJpaEntity> result = userDao.findByUsername("unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByEmail_existing_returnsUser() {
        Optional<UserJpaEntity> result = userDao.findByEmail("test@email.com");

        assertTrue(result.isPresent());
        assertEquals(user1.getUsername(), result.get().getUsername());
    }

    @Test
    void findByEmail_notFound_returnsEmpty() {
        Optional<UserJpaEntity> result = userDao.findByEmail("no@mail.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void insert_userWithoutCourses_success() {
        UserJpaEntity newUser = new UserJpaEntity(
                null,
                "newuser",
                "new@email.com",
                null,
                "encrypted",
                LocalDateTime.now(),
                null,
                UserRole.USER
        );

        UserJpaEntity saved = userDao.insert(newUser);
        entityManager.flush();

        assertNotNull(saved.getId());
    }

    @Test
    void insert_userWithExistingCourses_success() {
        long before = userDao.count();

        CourseJpaEntity course = new CourseJpaEntity(
                1L,
                "Learn Spanish from scratch",
                "A beginner course for Spanish learners.",
                new BigDecimal(20),
                "Spanish",
                "A2",
                10,
                null
        );

        UserJpaEntity user = new UserJpaEntity(
                null,
                "courseuser",
                "course@email.com",
                null,
                "encrypted",
                LocalDateTime.now(),
                null,
                UserRole.USER
        );
        user.getCoursesTaken().add(course);

        userDao.insert(user);
        entityManager.flush();

        long after = userDao.count();
        assertNotNull(user.getId());
        assertEquals(before + 1, after);

    }

    @Test
    void update_existingUser_success() {
        user1.setUsername("updatedName");

        userDao.update(user1);
        entityManager.flush();

        UserJpaEntity fromDb = entityManager.find(UserJpaEntity.class, user1.getId());

        assertEquals("updatedName", fromDb.getUsername());
    }

    @Test
    void update_notFound_throwsException() {
        UserJpaEntity fakeUser = new UserJpaEntity(
                999L,
                "fake",
                "fake@mail.com",
                null,
                "pwd",
                LocalDateTime.now(),
                null,
                UserRole.USER
        );

        assertThrows(RuntimeException.class,
                () -> userDao.update(fakeUser));
    }

    @Test
    void deleteById_existingUser_deletes() {
        userDao.deleteById(user1.getId());
        entityManager.flush();

        UserJpaEntity deleted =
                entityManager.find(UserJpaEntity.class, user1.getId());

        assertNull(deleted);
    }

    @Test
    void count_returnsCorrectValue() {
        long before = userDao.count();

        UserJpaEntity newUser = new UserJpaEntity(
                null,
                "count_user",
                "count_user@email.com",
                null,
                "encrypted",
                LocalDateTime.now(),
                null,
                UserRole.USER
        );
        userDao.insert(newUser);
        entityManager.flush();

        long after = userDao.count();
        assertEquals(before + 1, after);
    }

}
