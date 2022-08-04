package minjun.ddd.delivery.application.port.in;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryUsecaseTest {

  @Test
  void startDelivery() {
    // Delivery.status = STARTED
    // Order.state = DELIVERY_STARTED
  }

  @Test
  void completeDelivery() {
    // Delivery.status = COMPLETED
    // Order.state = DELIVERY_COMPLETED
  }
}