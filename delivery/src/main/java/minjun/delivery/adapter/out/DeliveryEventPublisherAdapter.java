package minjun.delivery.adapter.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.delivery.application.port.out.DeliveryEvent;
import minjun.delivery.application.port.out.DeliveryEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryEventPublisherAdapter implements DeliveryEventPublisher {

  private final ApplicationEventPublisher publisher;

  @Override
  public void publish(DeliveryEvent event) {
    publisher.publishEvent(event);
    log.info("Delivery Event Published: {} {}", event.getDelivery(), event.getTimestamp());
  }
}
