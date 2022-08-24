package minjun.payment.adapter.in;

import lombok.RequiredArgsConstructor;
import minjun.payment.application.port.in.CreatePaymentCommand;
import minjun.payment.application.port.in.PaymentDto;
import minjun.payment.application.port.in.PaymentUsecase;
import minjun.sharedkernel.domain.Money;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentUsecase paymentUsecase;

  public Long createPayment(Long orderId, String cardNo, Money amount) {
    return paymentUsecase.createPayment(new CreatePaymentCommand(orderId, cardNo, amount));
  }

  public Boolean cancelPayment(Long paymentId) {
    return paymentUsecase.cancelPayment(paymentId);
  }

  public PaymentDto getPayment(Long paymentId) {
    return paymentUsecase.getPayment(paymentId);
  }
}
