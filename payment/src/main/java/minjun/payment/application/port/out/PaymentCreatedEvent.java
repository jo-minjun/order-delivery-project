package minjun.payment.application.port.out;

import minjun.payment.domain.Payment;

public class PaymentCreatedEvent extends PaymentEvent {

  public PaymentCreatedEvent(Payment payment) {
    super(payment);
  }
}
