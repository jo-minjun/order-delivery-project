package minjun.ddd.order.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.ddd.common.domain.event.PaymentEvent;
import minjun.ddd.order.application.OrderHandlerService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventHandler {

  private final OrderHandlerService handlerService;

  @EventListener(value = PaymentEvent.class)
  public void handle(PaymentEvent event) {
    log.info("Payment Event Received: orderId={}, paymentId={}, timestamp={}",
        event.getOrderId(), event.getPaymentId(), event.getTimestamp());

    handlerService.handlePayment(event);
  }
}
