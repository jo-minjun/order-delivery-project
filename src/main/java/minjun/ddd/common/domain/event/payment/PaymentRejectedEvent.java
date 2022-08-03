package minjun.ddd.common.domain.event.payment;

public class PaymentRejectedEvent extends PaymentEvent {

  public PaymentRejectedEvent(Object source) {
    super(source);
  }
}
