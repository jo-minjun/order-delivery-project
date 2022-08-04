package minjun.ddd.order.application.port.out;

public interface OrderEventPublisher {

  void publish(OrderEvent event);
}


