package minjun.order.application.port.out;

import lombok.Getter;
import minjun.order.application.port.in.PayOrderCommand;
import minjun.order.domain.Order;

@Getter
public class OrderPaymentApprovedEvent extends OrderEvent {

  private final PayOrderCommand command;

  public OrderPaymentApprovedEvent(Order order, PayOrderCommand command) {
    super(order);
    this.command = command;
  }
}
