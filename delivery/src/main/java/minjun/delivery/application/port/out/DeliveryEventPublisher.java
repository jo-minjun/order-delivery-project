package minjun.delivery.application.port.out;

public interface DeliveryEventPublisher {

  void publish(DeliveryEvent event);
}
