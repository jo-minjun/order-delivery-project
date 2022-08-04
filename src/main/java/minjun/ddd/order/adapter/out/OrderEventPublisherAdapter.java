package minjun.ddd.order.adapter.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.ddd.order.application.port.out.OrderEvent;
import minjun.ddd.order.application.port.out.OrderEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisherAdapter implements OrderEventPublisher {

  private final ApplicationEventPublisher publisher;

  @Override
  public void publish(OrderEvent event) {
    publisher.publishEvent(event);
    log.info("Order Event Published: {} {}", event.getOrder(), event.getTimestamp());
  }
}
