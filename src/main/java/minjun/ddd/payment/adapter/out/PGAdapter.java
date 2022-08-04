package minjun.ddd.payment.adapter.out;

import java.util.UUID;

import minjun.ddd.common.Money;
import minjun.ddd.payment.application.port.out.PaymentPort;
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
