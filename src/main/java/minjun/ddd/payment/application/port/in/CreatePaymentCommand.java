package minjun.ddd.payment.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import minjun.ddd.common.Money;

@Data
@AllArgsConstructor
public class CreatePaymentCommand {

  private final Long orderId;
  private final String cardNo;
  private final Money amount;
}
