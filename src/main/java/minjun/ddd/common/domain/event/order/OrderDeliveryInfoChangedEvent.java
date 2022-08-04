package minjun.ddd.common.domain.event.order;

import lombok.Getter;
import lombok.ToString;
import minjun.ddd.common.domain.Address;
import minjun.ddd.order.domain.DeliveryInfo;
import minjun.ddd.order.domain.Order;

@Getter
@ToString(callSuper = true)
public class OrderDeliveryInfoChangedEvent extends OrderEvent {

  private final Long deliveryId;
  private final Address address;
  private final String phoneNumber;

  public OrderDeliveryInfoChangedEvent(Order order) {
    super(order);

    final DeliveryInfo deliveryInfo = order.getDeliveryInfo();
    this.deliveryId = deliveryInfo.getDeliveryId();
    this.address = deliveryInfo.getAddress();
    this.phoneNumber = deliveryInfo.getPhoneNumber();
  }
}