package es.speakly.store_backend.persistence.dao.jpa.impl;

import es.speakly.store_backend.domain.dto.LoginUserDto;
import es.speakly.store_backend.exceptions.ResourceNotFoundException;
import es.speakly.store_backend.persistence.dao.impl.AuthJpaDaoImpl;
import es.speakly.store_backend.persistence.dao.impl.entity.SessionJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static es.speakly.store_backend.domain.model.UserRole.USER;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = "spring.flyway.enabled=false")
@Import(AuthJpaDaoImpl.class)
public class AuthDaoJpaImplTest {
    @Autowired
    private AuthJpaDaoImpl authDao;

    @PersistenceContext
    private EntityManager entityManager;

    private UserJpaEntity userJpaEntity;

    @BeforeEach
    void setUp() {
        entityManager.createQuery("DELETE FROM SessionJpaEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM UserJpaEntity").executeUpdate();
        entityManager.flush();
        entityManager.clear();

        userJpaEntity = new UserJpaEntity(
                null,
                "testuser",
                "test@email.com",
                null,
                "encrypted-password",
                LocalDateTime.now(),
                null,
                USER
        );
        entityManager.persist(userJpaEntity);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByToken_success() {

        String token = "valid-token";

        SessionJpaEntity session = new SessionJpaEntity(token, userJpaEntity, LocalDateTime.now());
        entityManager.persist(session);
        entityManager.flush();
        entityManager.clear();

        Optional<LoginUserDto> result = authDao.findByToken(token);


        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(userJpaEntity.getId(), result.orElseThrow().id()),
                () -> assertEquals(userJpaEntity.getEmail(), result.orElseThrow().username()),
                () -> assertEquals(userJpaEntity.getRole(), result.orElseThrow().role())
        );
    }

    @Test
    void findByToken_notFound() {
        Optional<LoginUserDto> result = authDao.findByToken("invalid-token");
        assertTrue(result.isEmpty());
    }

    @Test
    void createTokenForUser_success() {
        Long before = authDao.count();

        UUID token = authDao.createTokenForUser(userJpaEntity.getId());

        Long after = authDao.count();

        assertAll(
                () -> assertNotNull(token),
                () -> assertEquals(before + 1, after)
        );
    }

    @Test
    void createTokenForUser_userNotFound_throwsException() {
        assertThrows(ResourceNotFoundException.class,
                () -> authDao.createTokenForUser(999L));
    }

    @Test
    void deleteToken_success() {
        String token = "token-to-delete";

        SessionJpaEntity session = new SessionJpaEntity(token, userJpaEntity, LocalDateTime.now());
        entityManager.persist(session);
        entityManager.flush();
        entityManager.clear();

        authDao.deleteToken(token);

        entityManager.flush();
        entityManager.clear();

        Long count = authDao.count();
        assertEquals(0L, count);
    }

}
