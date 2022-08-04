package minjun.ddd.order.application.port.in;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInfo {
  private String cardNo;
}
