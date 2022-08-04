package minjun.ddd.delivery.application.port.out;

public interface DeliveryEventPublisher {

  void publish(DeliveryEvent event);
}
