package minjun.payment.adapter.out;

import java.util.UUID;
import minjun.payment.application.port.out.PaymentPort;
import minjun.sharedkernel.domain.Money;
import org.springframework.stereotype.Component;

@Component
public class PGAdapter implements PaymentPort {

  @Override
  public String execute(String cardNo, Money amount) {
    return UUID.randomUUID().toString();
  }

  @Override
  public Boolean cancel(String authorizedNo) {
    return true;
  }
}
