package minjun.order.application.port.in;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import minjun.order.application.port.DeliveryInfo;
import minjun.order.application.port.PaymentInfo;

@Data
@Builder
public class OrderDto {

  private Long orderId;
  private Set<OrderItem> orderItems;
  private BigDecimal totalAmount;
  private DeliveryInfo delivery;
  private PaymentInfo payment;
  private String status;
}
