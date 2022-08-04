package minjun.ddd.order.adapter.out;

import lombok.AllArgsConstructor;
import minjun.ddd.common.Money;
import minjun.ddd.order.application.port.in.PaymentInfo;
import minjun.ddd.order.application.port.out.PaymentPort;
import minjun.ddd.payment.adapter.in.PaymentController;
import minjun.ddd.payment.application.port.in.PaymentDto;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SimulateApiCallPaymentAdapter implements PaymentPort {

  private final PaymentController paymentController;

  @Override
  public Long createPayment(Long orderId, String cardNo, Money amount) {
    // Simulate API call
    return paymentController.createPayment(orderId, cardNo, amount);
  }

  @Override
  public Boolean cancelPayment(Long paymentId) {
    // Simulate API call
    return paymentController.cancelPayment(paymentId);
  }

  @Override
  public PaymentInfo getPayment(Long paymentId) {
    // Simulate API call
    final PaymentDto paymentDto = paymentController.getPayment(paymentId);

    return PaymentInfo.builder()
        .paymentId(paymentDto.getId())
        .cardNo(paymentDto.getCardNo())
        .status(paymentDto.getStatus())
        .authorizedNo(paymentDto.getAuthorizedNo())
        .build();
  }
}