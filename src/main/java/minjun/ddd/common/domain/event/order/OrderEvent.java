package minjun.ddd.common.domain.event.order;

import lombok.Getter;
import lombok.ToString;
import minjun.ddd.order.domain.Order;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString(callSuper = false)
public class OrderEvent extends ApplicationEvent {

  private final Long orderId;

  public OrderEvent(Order order) {
    super(order);
    this.orderId = order.getId();
  }
}
