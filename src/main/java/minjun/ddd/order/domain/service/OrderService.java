package minjun.ddd.order.domain.service;

import minjun.ddd.common.domain.event.DeliveryEvent;
import minjun.ddd.common.domain.event.PaymentEvent;
import minjun.ddd.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderService {

  public void changeState(Order order, PaymentEvent event) {
    if (event.getIsApproved()) {
      order.approvePayment();
    }
  }

  public void changeState(Order order, DeliveryEvent event) {
    if (event.getIsCompleted()) {
      order.completeDelivery();
      return;
    }
    order.startDelivery();
  }
}
