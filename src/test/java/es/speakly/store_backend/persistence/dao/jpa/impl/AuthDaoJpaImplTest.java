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

@DataJpaTest
@Import(AuthJpaDaoImpl.class)
public class AuthDaoJpaImplTest {
    @Autowired
    private AuthJpaDaoImpl authDao;

    @PersistenceContext
    private EntityManager entityManager;

    private UserJpaEntity userJpaEntity;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void findByToken_success() {

        String token = "valid-token";

        SessionJpaEntity session =
                new SessionJpaEntity(token, userJpaEntity, LocalDateTime.now());
        entityManager.persist(session);
        entityManager.flush();

        Optional<LoginUserDto> result = authDao.findByToken(token);


        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(userJpaEntity.getId(), result.get().id()),
                () -> assertEquals(userJpaEntity.getEmail(), result.get().email()),
                () -> assertEquals(userJpaEntity.getRole(), result.get().role())
        );
    }

    @Test
    void findByToken_notFound() {
        Optional<LoginUserDto> result = authDao.findByToken("invalid-token");
        assertTrue(result.isEmpty());
    }

    @Test
    void createTokenForUser_success() {

        UUID token = authDao.createTokenForUser(userJpaEntity.getId());

        Long count = authDao.count();

        assertAll(
                () -> assertNotNull(token),
                () -> assertEquals(1L, count)
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

        SessionJpaEntity session =
                new SessionJpaEntity(token, userJpaEntity, LocalDateTime.now());
        entityManager.persist(session);
        entityManager.flush();

        authDao.deleteToken(token);

        Long count = authDao.count();
        assertEquals(0L, count);
    }

}
