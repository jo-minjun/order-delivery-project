package minjun.ddd.order.application.port.in;

import java.math.BigDecimal;
import java.util.Set;

public class OrderDto {

  private Long orderId;
  private Set<OrderItem> orderItems;
  private BigDecimal totalAmount;
  private DeliveryInfo delivery;
  private PaymentInfo payment;
  private String status;
}
