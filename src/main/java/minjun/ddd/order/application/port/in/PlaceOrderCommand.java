package minjun.ddd.order.application.port.in;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class PlaceOrderCommand {

  private Set<OrderItem> orderItems;
  private PaymentInfo paymentInfo;
  private DeliveryInfo deliveryInfo;
}
