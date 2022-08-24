package minjun.delivery.application.port.out;

import minjun.delivery.domain.Delivery;

public class DeliveryCompletedEvent extends DeliveryEvent {

  public DeliveryCompletedEvent(Delivery delivery) {
    super(delivery);
  }
}
