package minjun.ddd.payment.application.port.out;

import minjun.ddd.payment.domain.Payment;

public class PaymentCreatedEvent extends PaymentEvent {

  public PaymentCreatedEvent(Payment payment) {
    super(payment);
  }
}
