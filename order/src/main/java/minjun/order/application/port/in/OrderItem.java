package minjun.order.application.port.in;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItem {
  private Long productId;
  private String productName;
  private BigDecimal price;
  private Integer quantity;
}