package minjun.ddd.payment.application.port.out;

import minjun.ddd.payment.domain.Payment;

public class PaymentCancelledEvent extends PaymentEvent {

  public PaymentCancelledEvent(Payment payment) {
    super(payment);
  }
}
