package minjun.ddd.common.domain.event;

import lombok.Getter;
import lombok.ToString;
import minjun.ddd.payment.domain.Payment;
import minjun.ddd.payment.domain.PaymentStatus;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString(callSuper = false)
public class PaymentEvent extends ApplicationEvent {

  private final Long orderId;
  private final Long paymentId;
  private final Boolean isApproved;

  public PaymentEvent(Payment payment, Long orderId) {
    super(payment);
    this.paymentId = payment.getId();
    this.orderId = orderId;
    this.isApproved = payment.getStatus().equals(PaymentStatus.APPROVED);
  }
}
