package minjun.delivery.application.port.out;

import minjun.delivery.domain.Delivery;

public class DeliveryCreatedEvent extends DeliveryEvent {

  public DeliveryCreatedEvent(Delivery delivery) {
    super(delivery);
  }
}
