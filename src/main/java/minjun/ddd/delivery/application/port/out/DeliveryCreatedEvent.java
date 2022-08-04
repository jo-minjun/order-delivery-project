package minjun.ddd.delivery.application.port.out;

import minjun.ddd.delivery.domain.Delivery;

public class DeliveryCreatedEvent extends DeliveryEvent {

  public DeliveryCreatedEvent(Delivery delivery) {
    super(delivery);
  }
}
