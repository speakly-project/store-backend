package es.speakly.store_backend.spring;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import es.speakly.store_backend.domain.repository.*;
import es.speakly.store_backend.domain.service.*;
import es.speakly.store_backend.domain.service.impl.*;
import es.speakly.store_backend.domain.usecase.PasswdUpdateUseCase;
import es.speakly.store_backend.filters.AuthFilter;
import es.speakly.store_backend.nanoServices.http.impl.HttpClientServiceImpl;
import es.speakly.store_backend.nanoServices.payment.CardPaymentService;
import es.speakly.store_backend.nanoServices.http.HttpClientService;
import es.speakly.store_backend.nanoServices.payment.CardPaymentServiceImpl;
import es.speakly.store_backend.persistence.dao.*;
import es.speakly.store_backend.persistence.dao.impl.*;
import es.speakly.store_backend.persistence.repository.*;
import es.speakly.store_backend.usecase.PasswdUpdateUseCaseImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SpringConfig {
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Value("${bank.api.base-url}")
    private String bankApiBaseUrl;

    @Value("${bank.api.username}")
    private String bankApiUsername;

    @Value("${bank.api.key}")
    private String bankApiKey;

    @Value("${bank.api.destination-iban}")
    private String bankApiDestinationIban;

    @Bean
    public Cloudinary cloudinary() {
        Map config = ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        );
        return new Cloudinary(config);
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter(AuthService authService) {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AuthFilter(authService));
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public PasswdUpdateUseCase passwdUpdateUseCase(UserRepository userRepository) {
        return new PasswdUpdateUseCaseImpl(userRepository);
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

    @Bean
    public CartService cartService(OrderRepository orderRepository, UserService userService, CourseService courseService, CardPaymentService cardPaymentService) {
        return new CartServiceImpl(orderRepository, userService, courseService, cardPaymentService);
    }

    @Bean
    public OrderRepository orderRepository(OrderDao orderDao) {
        return new OrderRepositoryImpl(orderDao);
    }

    @Bean
    public OrderDao orderDao() {
        return new OrderJpaDaoImpl();
    }

}