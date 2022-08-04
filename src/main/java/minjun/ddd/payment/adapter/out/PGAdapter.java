package minjun.ddd.payment.adapter.out;

import java.util.UUID;
import minjun.ddd.payment.application.port.out.ExecutePayment;
import org.springframework.stereotype.Component;

@Component
public class PGAdapter implements ExecutePayment {

  @Override
  public String execute(String cardNo) {

    // 외부 연동 로직

    return UUID.randomUUID().toString();
  }
}
