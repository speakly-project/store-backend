package es.speakly.store_backend.nanoServices.http;

///api/speakly-bank/payments
public interface HttpClientService {
    CardPaymentResponse post(String url, Object requestBody);
}
