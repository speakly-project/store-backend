package es.speakly.store_backend.spring;


import es.speakly.store_backend.domain.repository.UserRepository;
import es.speakly.store_backend.domain.service.UserService;
import es.speakly.store_backend.domain.service.impl.UserServiceImpl;
import es.speakly.store_backend.persistence.dao.Impl.entity.UserJpaEntity;
import es.speakly.store_backend.persistence.repository.UserRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SpringConfig{
    @Bean
    public UserService userService(UserRepository userRepository){
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public UserRepository userRepository(UserJpaEntity userJpaEntity){
        return new UserRepositoryImpl(userJpaEntity);
    }

    @Bean
    public UserJpaEntity userJpaEntity(){
        return new UserJpaEntity();
    }

}