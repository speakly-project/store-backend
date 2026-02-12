package es.speakly.store_backend.nanoServices.http;

import es.speakly.store_backend.nanoServices.payment.dtos.DestinationDto;
import es.speakly.store_backend.nanoServices.payment.dtos.OriginDto;
import es.speakly.store_backend.nanoServices.payment.dtos.PaymentDto;

public class CardPaymentResponse {

    private OriginDto origin;

    private DestinationDto destination;

    private PaymentDto payment;

    private String status;

    public CardPaymentResponse() {
    }

    public CardPaymentResponse(OriginDto origin, DestinationDto destination, PaymentDto payment, String status) {
        this.origin = origin;
        this.destination = destination;
        this.payment = payment;
        this.status = status;
    }

    public OriginDto getOrigin() {
        return origin;
    }

    public void setOrigin(OriginDto origin) {
        this.origin = origin;
    }

    public DestinationDto getDestination() {
        return destination;
    }

    public void setDestination(DestinationDto destination) {
        this.destination = destination;
    }

    public PaymentDto getPayment() {
        return payment;
    }

    public void setPayment(PaymentDto payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return "SUCCESS".equals(status);
    }

    @Override
    public String toString() {
        return "CardPaymentResponse{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", payment=" + payment +
                ", status='" + status + '\'' +
                '}';
    }
}
