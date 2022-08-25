package minjun.order.application.port;

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
