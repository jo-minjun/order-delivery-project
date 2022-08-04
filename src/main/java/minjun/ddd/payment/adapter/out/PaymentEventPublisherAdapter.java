package minjun.ddd.payment.adapter.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.ddd.payment.application.port.out.PaymentEvent;
import minjun.ddd.payment.application.port.out.PaymentEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventPublisherAdapter implements PaymentEventPublisher {

  private final ApplicationEventPublisher publisher;

  @Override
  public void publish(PaymentEvent event) {
    publisher.publishEvent(event);
    log.info("Order Event Published: {} {}", event.getPayment(), event.getTimestamp());
  }
}
