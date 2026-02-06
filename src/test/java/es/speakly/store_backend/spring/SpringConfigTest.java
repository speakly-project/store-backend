package es.speakly.store_backend.spring;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@TestConfiguration
public class SpringConfigTest {

    @Bean
    public Cloudinary cloudinary() {
        @SuppressWarnings("rawtypes")
        Map config = ObjectUtils.asMap(
                "cloud_name", "test_cloud",
                "api_key", "test_key",
                "api_secret", "test_secret"
        );
        return new Cloudinary(config);
    }
}
