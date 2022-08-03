package minjun.ddd.common.domain.event.order;

import lombok.Getter;
import lombok.ToString;
import minjun.ddd.order.domain.Order;

@Getter
@ToString(callSuper = true)
public class OrderDeliveryStartedEvent extends OrderEvent {

  private final Long deliveryId;

  public OrderDeliveryStartedEvent(Order order) {
    super(order);
    this.deliveryId = order.getDeliveryInfo().getDeliveryId();
  }
}
