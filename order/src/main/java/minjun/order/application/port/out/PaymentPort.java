package minjun.order.application.port.out;

import minjun.order.application.port.in.PaymentInfo;
import minjun.sharedkernel.domain.Money;

public interface PaymentPort {

  Long createPayment(Long orderId, String cardNo, Money amount);
  Boolean cancelPayment(Long paymentId);
  PaymentInfo getPayment(Long paymentId);
}
