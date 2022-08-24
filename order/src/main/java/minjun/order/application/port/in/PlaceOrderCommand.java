package minjun.order.application.port.in;

import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceOrderCommand {

  private Set<minjun.order.application.port.in.OrderItem> orderItems;
  private minjun.order.application.port.in.PaymentInfo paymentInfo;
  private minjun.order.application.port.in.DeliveryInfo deliveryInfo;
}
