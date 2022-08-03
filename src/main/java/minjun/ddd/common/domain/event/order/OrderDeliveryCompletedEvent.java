package minjun.ddd.common.domain.event.order;

import minjun.ddd.order.domain.Order;

public class OrderDeliveryCompletedEvent extends OrderEvent {

  public OrderDeliveryCompletedEvent(Order order) {
    super(order);
  }
}
