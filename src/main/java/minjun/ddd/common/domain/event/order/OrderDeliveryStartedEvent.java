package minjun.ddd.common.domain.event.order;

import minjun.ddd.order.domain.Order;

public class OrderDeliveryStartedEvent extends OrderEvent {

  public OrderDeliveryStartedEvent(Order order) {
    super(order);
  }
}
