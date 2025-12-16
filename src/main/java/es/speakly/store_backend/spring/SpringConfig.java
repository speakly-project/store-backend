package es.speakly.store_backend.spring;


import es.speakly.store_backend.domain.repository.AuthRepository;
import es.speakly.store_backend.domain.repository.CourseRepository;
import es.speakly.store_backend.domain.repository.LevelRepository;
import es.speakly.store_backend.domain.repository.UserRepository;
import es.speakly.store_backend.domain.service.AuthService;
import es.speakly.store_backend.domain.service.CourseService;
import es.speakly.store_backend.domain.service.LanguageService;
import es.speakly.store_backend.domain.service.LevelService;
import es.speakly.store_backend.domain.service.UserService;
import es.speakly.store_backend.domain.service.impl.AuthServiceImpl;
import es.speakly.store_backend.domain.service.impl.CourseServiceImpl;
import es.speakly.store_backend.domain.service.impl.LanguageServiceImpl;
import es.speakly.store_backend.domain.service.impl.LevelServiceImpl;
import es.speakly.store_backend.domain.service.impl.UserServiceImpl;
import es.speakly.store_backend.filters.AuthFilter;
import es.speakly.store_backend.persistence.dao.AuthDao;
import es.speakly.store_backend.persistence.dao.CourseDao;
import es.speakly.store_backend.persistence.dao.LanguageDao;
import es.speakly.store_backend.persistence.dao.LevelDao;
import es.speakly.store_backend.persistence.dao.UserDao;
import es.speakly.store_backend.persistence.dao.impl.AuthJpaDaoImpl;
import es.speakly.store_backend.persistence.dao.impl.CourseDaoJpaImpl;
import es.speakly.store_backend.persistence.dao.impl.LanguageDaoJpaImpl;
import es.speakly.store_backend.persistence.dao.impl.LevelDaoJpaImpl;
import es.speakly.store_backend.persistence.dao.impl.UserDaoJpaImpl;
import es.speakly.store_backend.persistence.repository.AuthRepositoryImpl;
import es.speakly.store_backend.persistence.repository.CourseRepositoryImpl;
import es.speakly.store_backend.persistence.repository.LanguageRepositoryImpl;
import es.speakly.store_backend.persistence.repository.LevelRepositoryImpl;
import es.speakly.store_backend.persistence.repository.UserRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter(AuthService authService) {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AuthFilter(authService));
        registration.addUrlPatterns("/*");
        return registration;
    }

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
    
    @Bean
    public AuthDao authDao() {
        return new AuthJpaDaoImpl();
    }

    @Bean
    public AuthRepository authRepository(AuthDao authDao){
        return new AuthRepositoryImpl(authDao);
    }

    @Bean
    public AuthService authService(UserRepository userRepository, AuthRepository authRepository) {
        return new AuthServiceImpl(userRepository, authRepository);
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

    // Level beans
    @Bean
    public LevelDao levelDao() {
        return new LevelDaoJpaImpl();
    }
    @Bean
    public LevelRepository levelRepository(LevelDao levelDao) {
        return new LevelRepositoryImpl(levelDao);
    }
    @Bean
    public LevelService levelService(LevelRepository levelRepository) {
        return new LevelServiceImpl(levelRepository);
    }
}