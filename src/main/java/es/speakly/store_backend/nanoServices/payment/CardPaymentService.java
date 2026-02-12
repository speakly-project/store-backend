package es.speakly.store_backend.nanoServices.payment;



import es.speakly.store_backend.nanoServices.payment.dtos.OriginDto;

import java.math.BigDecimal;

public interface CardPaymentService {
    void processPayment(OriginDto creditCard, BigDecimal amount);
}
