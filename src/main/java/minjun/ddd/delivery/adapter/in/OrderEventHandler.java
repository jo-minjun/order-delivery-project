package minjun.ddd.delivery.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.ddd.common.domain.event.order.OrderDeliveryInfoChangedEvent;
import minjun.ddd.common.domain.event.order.OrderPaymentApprovedEvent;
import minjun.ddd.delivery.application.DeliveryHandlerService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component(value = "DeliveryOrderEventHander")
public class OrderEventHandler {

  private final DeliveryHandlerService handlerService;

  @EventListener(value = OrderPaymentApprovedEvent.class)
  public void handle(OrderPaymentApprovedEvent event) {
    log.info("Payment Event Received: orderId={}, deliveryId={}, timestamp={}",
        event.getOrderId(), event.getOrderId(), event.getTimestamp());

    handlerService.createDelivery(event);
  }

  @EventListener(value = OrderDeliveryInfoChangedEvent.class)
  public void handle(OrderDeliveryInfoChangedEvent event) {
    log.info("Payment Event Received: orderId={}, deliveryId={}, timestamp={}",
        event.getOrderId(), event.getDeliveryId(), event.getTimestamp());

    handlerService.changeDeliveryInfo(event);
  }
}
