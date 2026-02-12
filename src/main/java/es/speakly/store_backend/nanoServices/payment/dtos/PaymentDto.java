package es.speakly.store_backend.nanoServices.payment.dtos;
import java.math.BigDecimal;

public record PaymentDto(
        BigDecimal amount,
        String description
) {

}
