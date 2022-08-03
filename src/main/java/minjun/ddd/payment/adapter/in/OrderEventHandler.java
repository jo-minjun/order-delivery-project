package minjun.ddd.payment.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.ddd.common.domain.event.order.OrderCreatedEvent;
import minjun.ddd.payment.application.PaymentHandlerService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventHandler {

  private final PaymentHandlerService handlerService;

  @EventListener(value = OrderCreatedEvent.class)
  public void handle(OrderCreatedEvent event) {
    log.info("Payment Event Received: orderId={}, timestamp={}",
        event.getOrderId(), event.getTimestamp());
    handlerService.pay(event);
  }
}
