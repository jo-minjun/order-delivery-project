package minjun.order.application.port.in;

import java.util.Set;
import lombok.Builder;
import lombok.Data;
import minjun.order.application.port.DeliveryInfo;
import minjun.order.application.port.PaymentInfo;

@Data
@Builder
public class PlaceOrderCommand {

  private Set<minjun.order.application.port.in.OrderItem> orderItems;
  private PaymentInfo paymentInfo;
  private DeliveryInfo deliveryInfo;
}
