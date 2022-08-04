package minjun.ddd.payment.domain.service;

import lombok.RequiredArgsConstructor;
import minjun.ddd.payment.application.port.out.ExecutePayment;
import minjun.ddd.payment.domain.Payment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentService {

  private final ExecutePayment executePayment;

  public void pay(Payment payment, Long orderId) {
    try {
      final String authorizedNo = executePayment.execute(payment.getCardNo());
      payment.approvedPayment(authorizedNo, orderId);
    } catch (RuntimeException e) {
      payment.rejectedPayment(orderId);
    }
  }
}
