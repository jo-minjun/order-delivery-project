package minjun.ddd.payment.application.port.out;

import minjun.ddd.payment.domain.Payment;

public class PaymentApprovedEvent extends PaymentEvent {

  public PaymentApprovedEvent(Payment payment) {
    super(payment);
  }
}
