package minjun.payment.application.port.out;

public interface PaymentEventPublisher {

  void publish(PaymentEvent event);
}
