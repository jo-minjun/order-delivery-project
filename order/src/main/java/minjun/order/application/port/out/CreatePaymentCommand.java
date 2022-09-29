package minjun.order.application.port.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import minjun.sharedkernel.domain.Money;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentCommand {

  private Long orderId;
  private String cardNo;
  private Money amount;
}
