package minjun.ddd.order.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.ddd.common.domain.event.DeliveryEvent;
import minjun.ddd.order.application.OrderHandlerService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryEventHandler {

  private final OrderHandlerService handlerService;

  @EventListener(value = DeliveryEvent.class)
  public void handle(DeliveryEvent event) {
    log.info("Delivery Event Received: orderId={}, deliveryId={}, timestamp={}",
        event.getOrderId(), event.getDeliveryId(), event.getTimestamp());

    handlerService.handleDelivery(event);
  }
}
