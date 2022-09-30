package minjun.payment.adapter.out;

import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import minjun.payment.application.port.out.PaymentPort;
import minjun.sharedkernel.domain.Money;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PGAdapter implements PaymentPort {

  @Override
  public Optional<String> execute(String cardNo, Money amount) {
    try {
      return Optional.of(UUID.randomUUID().toString());
    } catch (Exception e) {
      log.error("결제 진행 중 예기치 못한 오류가 발생했습니다.");
      return Optional.empty();
    }
  }

  @Override
  public Boolean cancel(String authorizedNo) {
    try {
      return true;
    } catch (Exception e) {
      log.error("환불 진행 중 예기치 못한 오류가 발생했습니다.");
      return false;
    }
  }
}
