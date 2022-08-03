package minjun.ddd.common.domain.event.order;

import minjun.ddd.order.domain.Order;
import org.springframework.context.ApplicationEvent;

public class OrderEvent extends ApplicationEvent {

  public OrderEvent(Order order) {
    super(order);
  }
}
