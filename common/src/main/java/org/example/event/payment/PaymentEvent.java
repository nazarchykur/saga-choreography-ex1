package org.example.event.payment;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dto.PaymentDto;
import org.example.event.Event;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PaymentEvent implements Event {

    private final UUID eventId = UUID.randomUUID();
    private final Date date = new Date();
    private PaymentDto payment;
    private PaymentStatus paymentStatus;

    public PaymentEvent(PaymentDto payment, PaymentStatus status) {
        this.payment = payment;
        this.paymentStatus = status;
    }

    @Override
    public UUID getEventId() {
        return this.eventId;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    public PaymentDto getPayment() {
        return payment;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
}
