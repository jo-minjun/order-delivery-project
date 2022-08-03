package minjun.ddd.common.domain.event.order;

import minjun.ddd.order.domain.Order;

public class OrderCreatedEvent extends OrderEvent {


  public OrderCreatedEvent(Order order) {
    super(order);
  }
}
