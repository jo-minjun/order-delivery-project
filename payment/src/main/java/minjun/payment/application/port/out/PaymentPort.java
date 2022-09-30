package minjun.payment.application.port.out;

import java.util.Optional;
import minjun.sharedkernel.domain.Money;

public interface PaymentPort {

  Optional<String> execute(String cardNo, Money amount);
  Boolean cancel(String authorizedNo);
}
