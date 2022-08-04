package minjun.ddd.delivery.application.port.out;

import minjun.ddd.delivery.domain.Delivery;

public class DeliveryCompletedEvent extends DeliveryEvent {

  public DeliveryCompletedEvent(Delivery delivery) {
    super(delivery);
  }
}
