package minjun.ddd.common.domain.event.order;

import minjun.ddd.order.domain.Order;

public class OrderCancelledEvent extends OrderEvent {

  public OrderCancelledEvent(Order order) {
    super(order);
  }
}
