package minjun.ddd.common.domain.event;

import lombok.Getter;
import lombok.ToString;
import minjun.ddd.delivery.domain.Delivery;
import minjun.ddd.delivery.domain.DeliveryStatus;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString(callSuper = false)
public class DeliveryEvent extends ApplicationEvent {

  private final Long orderId;
  private final Long deliveryId;
  private final Boolean isCompleted;

  public DeliveryEvent(Delivery delivery, Long orderId) {
    super(delivery);
    this.orderId = orderId;
    this.deliveryId = delivery.getId();
    this.isCompleted = delivery.getStatus().equals(DeliveryStatus.COMPLETED);
  }
}
