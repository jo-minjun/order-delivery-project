package minjun.order.application.port.in;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItem {
  private Long productId;
  private String productName;
  private BigDecimal price;
  private Integer quantity;
}