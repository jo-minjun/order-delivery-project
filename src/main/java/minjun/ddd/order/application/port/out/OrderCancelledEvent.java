package minjun.ddd.order.application.port.out;

import lombok.Getter;
import minjun.ddd.order.domain.Order;

@Getter
public class OrderCancelledEvent extends OrderEvent {

  public OrderCancelledEvent(Order order) {
    super(order);
  }
}
