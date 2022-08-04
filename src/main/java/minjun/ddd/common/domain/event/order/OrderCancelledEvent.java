package minjun.ddd.common.domain.event.order;

import lombok.Getter;
import lombok.ToString;
import minjun.ddd.order.domain.Order;

@Getter
@ToString(callSuper = true)
public class OrderCancelledEvent extends OrderEvent {

  private final Long paymentId;

  public OrderCancelledEvent(Order order) {
    super(order);
    this.paymentId = order.getPaymentId();
  }
}
