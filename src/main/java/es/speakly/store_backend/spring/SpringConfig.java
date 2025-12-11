package es.speakly.store_backend.spring;


import es.speakly.store_backend.domain.repository.CourseRepository;
import es.speakly.store_backend.domain.repository.UserRepository;
import es.speakly.store_backend.domain.service.CourseService;
import es.speakly.store_backend.domain.service.LanguageService;
import es.speakly.store_backend.domain.service.UserService;
import es.speakly.store_backend.domain.service.impl.CourseServiceImpl;
import es.speakly.store_backend.domain.service.impl.LanguageServiceImpl;
import es.speakly.store_backend.domain.service.impl.UserServiceImpl;
import es.speakly.store_backend.persistence.dao.CourseDao;
import es.speakly.store_backend.persistence.dao.LanguageDao;
import es.speakly.store_backend.persistence.dao.UserDao;
import es.speakly.store_backend.persistence.dao.impl.CourseDaoJpaImpl;
import es.speakly.store_backend.persistence.dao.impl.LanguageDaoJpaImpl;
import es.speakly.store_backend.persistence.dao.impl.UserDaoJpaImpl;
import es.speakly.store_backend.persistence.repository.CourseRepositoryImpl;
import es.speakly.store_backend.persistence.repository.LanguageRepositoryImpl;
import es.speakly.store_backend.persistence.repository.UserRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    // User beans
    @Bean
    public UserDao userDao() {
        return new UserDaoJpaImpl();
    }

    @Bean
    public UserRepository userRepository(UserDao userDao) {
        return new UserRepositoryImpl(userDao);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    // Course beans
    @Bean
    public CourseDao courseDao() {
        return new CourseDaoJpaImpl();
    }

    @Bean
    public CourseRepository courseRepository(CourseDao courseDao) {
        return new CourseRepositoryImpl(courseDao);
    }

    @Bean
    public CourseService courseService(CourseRepository courseRepository) {
        return new CourseServiceImpl(courseRepository);
    }
    //Language beans
    @Bean
    public LanguageDao languageDao() {
        return new LanguageDaoJpaImpl();
    }
    @Bean
    public LanguageRepositoryImpl languageRepository(LanguageDao languageDao) {
        return new LanguageRepositoryImpl(languageDao);
    }
    @Bean
    public LanguageService languageService(LanguageRepositoryImpl languageRepository) {
        return new LanguageServiceImpl(languageRepository);
    }
}