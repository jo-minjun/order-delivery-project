package minjun.payment.application.port.out;

import minjun.payment.domain.Payment;

public class PaymentCancelledEvent extends PaymentEvent {

  public PaymentCancelledEvent(Payment payment) {
    super(payment);
  }
}
