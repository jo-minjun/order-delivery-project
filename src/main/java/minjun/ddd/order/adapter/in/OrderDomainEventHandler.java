package minjun.ddd.order.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.ddd.delivery.application.port.out.DeliveryCompletedEvent;
import minjun.ddd.delivery.application.port.out.DeliveryStartedEvent;
import minjun.ddd.order.application.port.in.OrderUsecase;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderDomainEventHandler {

  private final OrderUsecase orderUsecase;

  @EventListener(DeliveryStartedEvent.class)
  public void handleDeliveryStartedEvent(DeliveryStartedEvent event) {
    orderUsecase.startDelivery(event.getDelivery().getOrderId());
  }

  @EventListener(DeliveryCompletedEvent.class)
  public void handleDeliveryCompletedEvent(DeliveryCompletedEvent event) {
    orderUsecase.completeDelivery(event.getDelivery().getOrderId());
  }
}
