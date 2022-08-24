package minjun.payment.application.port.out;

import minjun.payment.domain.Payment;

public class PaymentApprovedEvent extends PaymentEvent {

  public PaymentApprovedEvent(Payment payment) {
    super(payment);
  }
}
