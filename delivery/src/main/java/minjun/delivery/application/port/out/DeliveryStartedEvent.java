package minjun.delivery.application.port.out;

import minjun.delivery.domain.Delivery;

public class DeliveryStartedEvent extends DeliveryEvent{

  public DeliveryStartedEvent(Delivery delivery) {
    super(delivery);
  }
}
