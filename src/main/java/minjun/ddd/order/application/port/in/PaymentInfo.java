package minjun.ddd.order.application.port.in;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInfo {
  private Long paymentId;
  private String cardNo;
  private String authorizedNo;
  private String status;
}
