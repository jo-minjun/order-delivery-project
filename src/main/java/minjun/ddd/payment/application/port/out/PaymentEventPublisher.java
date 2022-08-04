package minjun.ddd.payment.application.port.out;

public interface PaymentEventPublisher {

  void publish(PaymentEvent event);
}
