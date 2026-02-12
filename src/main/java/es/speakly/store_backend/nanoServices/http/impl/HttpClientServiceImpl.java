package es.speakly.store_backend.nanoServices.http.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.speakly.store_backend.exceptions.BusinessException;
import es.speakly.store_backend.nanoServices.http.CardPaymentResponse;
import es.speakly.store_backend.nanoServices.http.HttpClientService;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpClientServiceImpl implements HttpClientService {
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public  HttpClientServiceImpl() {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public CardPaymentResponse post(String url, Object requestBody) {
        try {
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

            return httpClient.execute(httpPost, response -> {
                int statusCode = response.getCode();

                if (statusCode < 200 || statusCode >= 300) {
                    String errorBody = EntityUtils.toString(response.getEntity());
                    throw new BusinessException(
                            "HTTP request failed with status " + statusCode + ": " + errorBody
                    );
                }
                String responseBody = EntityUtils.toString(response.getEntity());
                return objectMapper.readValue(responseBody, CardPaymentResponse.class);
            });
        } catch (BusinessException e) {
            throw e;
        } catch (IOException e) {
            throw new BusinessException("Error during HTTP request to " + url + ": " + e.getMessage());
        }
    }
//¿¿¿¿¿¿¿¿¿¿
    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            // Log and ignore
        }
    }
}
