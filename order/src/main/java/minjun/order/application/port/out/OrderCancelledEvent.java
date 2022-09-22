package minjun.order.application.port.out;

import lombok.Getter;
import minjun.order.domain.Order;

@Getter
public class OrderCancelledEvent extends OrderEvent {

  public OrderCancelledEvent(Order order) {
    super(order);
  }
}
