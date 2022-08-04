package minjun.ddd.payment.application.port.in;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDto {

  private Long id;
  private String cardNo;
  private String status;
  private String authorizedNo;
  private Long orderId;
}
