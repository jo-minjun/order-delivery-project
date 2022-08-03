package minjun.ddd.order.application;

import lombok.RequiredArgsConstructor;
import minjun.ddd.common.domain.event.DeliveryEvent;
import minjun.ddd.common.domain.event.PaymentEvent;
import minjun.ddd.order.domain.Order;
import minjun.ddd.order.domain.OrderRepository;
import minjun.ddd.order.domain.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderHandlerService {

  private final OrderRepository orderRepository;
  private final OrderService orderService;

  @Transactional
  public void handlePayment(PaymentEvent event) {
    final Long orderId = event.getOrderId();
    final Long paymentId = event.getPaymentId();

    final Order order = orderRepository.findById(orderId).orElseThrow(
        () -> new RuntimeException("Not Found OrderId: " + orderId + "PaymentId: " + paymentId));

    order.associatePayment(paymentId);
    orderService.changeState(order, event);
  }

  @Transactional
  public void handleDelivery(DeliveryEvent event) {
    final Long orderId = event.getOrderId();
    final Long deliveryId = event.getDeliveryId();

    final Order order = orderRepository.findById(orderId).orElseThrow(
        () -> new RuntimeException("Not Found OrderId: " + orderId + "DeliveryId: " + deliveryId));

    order.associateDelivery(deliveryId);
    orderService.changeState(order, event);
  }
}
