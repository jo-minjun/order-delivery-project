package minjun.order.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentInfo {
  private Long paymentId;
  private String cardNo;
  private String authorizedNo;
  private String status;
}
