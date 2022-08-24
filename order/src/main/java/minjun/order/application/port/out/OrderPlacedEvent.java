package minjun.order.application.port.out;

import lombok.Getter;
import minjun.order.application.port.in.PlaceOrderCommand;
import minjun.order.domain.Order;

@Getter
public class OrderPlacedEvent extends OrderEvent {

  private PlaceOrderCommand command;

  public OrderPlacedEvent(Order order, PlaceOrderCommand command) {
    super(order);
    this.command = command;
  }
}
