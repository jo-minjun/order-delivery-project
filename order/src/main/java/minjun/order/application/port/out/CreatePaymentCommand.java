package minjun.order.application.port.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import minjun.sharedkernel.domain.Money;

@Data
@AllArgsConstructor
public class CreatePaymentCommand {

  private final Long orderId;
  private final String cardNo;
  private final Money amount;
}
