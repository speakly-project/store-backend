package es.speakly.store_backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.speakly.store_backend.spring.SpringConfigTest;
import es.speakly.store_backend.annotations.AuthenticationInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UploadImgController.class)
@Import(SpringConfigTest.class)
public class UploadImgControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Cloudinary cloudinary;

    @MockitoBean
    private AuthenticationInterceptor authenticationInterceptor;

    @BeforeEach
    void setUp() throws Exception {
        Mockito.when(authenticationInterceptor.preHandle(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(true);
    }

    @Test
    void getSignature_ShouldReturnSignatureData() throws Exception {
        var mvcResult = mockMvc.perform(get("/api/speakly/upload/signature"))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(content);

        assertThat(node.has("timestamp")).isTrue();
        assertThat(node.has("signature")).isTrue();
        assertThat(node.has("apiKey")).isTrue();
        assertThat(node.has("cloudName")).isTrue();

        long timestamp = node.get("timestamp").asLong();
        String signature = node.get("signature").asText();
        String apiKey = node.get("apiKey").asText();
        String cloudName = node.get("cloudName").asText();

        assertThat(apiKey).isEqualTo(cloudinary.config.apiKey);
        assertThat(cloudName).isEqualTo(cloudinary.config.cloudName);

        @SuppressWarnings("unchecked")
        Map<String, Object> params = ObjectUtils.asMap(
                "timestamp", timestamp,
                "folder", "profiles"
        );
        String expectedSignature = cloudinary.apiSignRequest(params, cloudinary.config.apiSecret);

        assertThat(signature).isEqualTo(expectedSignature);
    }
}
