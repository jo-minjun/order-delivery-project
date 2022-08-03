package minjun.ddd.order.adapter.in;

import lombok.RequiredArgsConstructor;
import minjun.ddd.common.PaymentEvent;
import minjun.ddd.order.application.OrderHandlerService;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class PaymentEventHandler {

  private final OrderHandlerService orderHandlerService;

  @EventListener(value = PaymentEvent.class)
  public void handle(PaymentEvent event) {
    orderHandlerService.handlePaymentEvent(event);
  }
}
