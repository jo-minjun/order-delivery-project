package minjun.ddd.delivery.application.port.out;

import minjun.ddd.delivery.domain.Delivery;

public class DeliveryStartedEvent extends DeliveryEvent{

  public DeliveryStartedEvent(Delivery delivery) {
    super(delivery);
  }
}
