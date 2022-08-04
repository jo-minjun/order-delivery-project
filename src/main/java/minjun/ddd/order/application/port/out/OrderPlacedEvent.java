package minjun.ddd.order.application.port.out;

import lombok.Getter;
import minjun.ddd.order.application.port.in.PlaceOrderCommand;
import minjun.ddd.order.domain.Order;

@Getter
public class OrderPlacedEvent extends OrderEvent {

  private PlaceOrderCommand command;

  public OrderPlacedEvent(Order order, PlaceOrderCommand command) {
    super(order);
    this.command = command;
  }
}
