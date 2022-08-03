package minjun.ddd.common.domain.event.order;

import minjun.ddd.order.domain.Order;

public class OrderPaymentApprovedEvent extends OrderEvent {

  public OrderPaymentApprovedEvent(Order order) {
    super(order);
  }
}
