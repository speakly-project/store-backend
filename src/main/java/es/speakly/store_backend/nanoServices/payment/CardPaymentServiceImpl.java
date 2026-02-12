package es.speakly.store_backend.nanoServices.payment;


import es.speakly.store_backend.nanoServices.http.HttpClientService;
import es.speakly.store_backend.nanoServices.http.CardPaymentResponse;
import es.speakly.store_backend.nanoServices.payment.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CardPaymentServiceImpl implements CardPaymentService {

    @Autowired
    private HttpClientService httpClient;

    @Value("${bank.api.base-url}")
    private String bankApiUrl;

    @Value("${bank.api.key}")
    private String bankApiKey;

    @Value("${bank.api.username}")
    private String bankApiUsername;

    @Value("${bank.api.destination-iban}")
    private String bankApiIban;

    @Override
    public void processPayment(OriginDto creditCard, BigDecimal amount) {
        AuthorizationDto authRequest = new AuthorizationDto(bankApiUsername, bankApiKey);
        DestinationDto transferRequest = new DestinationDto(bankApiIban);
        PaymentDto pago = new PaymentDto(amount, "Compra en speakly");

        CardPaymentDto paymentRequest =
                new CardPaymentDto(authRequest, creditCard, transferRequest, pago, null);
        CardPaymentResponse response = httpClient.post(
                bankApiUrl + "/api/speakly-bank/payments",
                paymentRequest
        );

    }
}
