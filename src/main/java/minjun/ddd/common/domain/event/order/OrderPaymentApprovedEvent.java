package minjun.ddd.common.domain.event.order;

import lombok.Getter;
import lombok.ToString;
import minjun.ddd.common.domain.Address;
import minjun.ddd.order.domain.DeliveryInfo;
import minjun.ddd.order.domain.Order;

@Getter
@ToString(callSuper = true)
public class OrderPaymentApprovedEvent extends OrderEvent {

  private final Address address;
  private final String phoneNumber;

  public OrderPaymentApprovedEvent(Order order) {
    super(order);

    final DeliveryInfo deliveryInfo = order.getDeliveryInfo();
    this.address = deliveryInfo.getAddress();
    this.phoneNumber = deliveryInfo.getPhoneNumber();
  }
}
