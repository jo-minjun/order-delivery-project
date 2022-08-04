package minjun.ddd.common.domain.event.order;

import lombok.Getter;
import lombok.ToString;
import minjun.ddd.common.domain.Money;
import minjun.ddd.order.domain.Order;

@Getter
@ToString(callSuper = true)
public class OrderCreatedEvent extends OrderEvent {

  private final String cardNo;
  private final Money totalAmount;

  public OrderCreatedEvent(Order order, String cardNo, Money totalAmount) {
    super(order);
    this.cardNo = cardNo;
    this.totalAmount = totalAmount;
  }
}
