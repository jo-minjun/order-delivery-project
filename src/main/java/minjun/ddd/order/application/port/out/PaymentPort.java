package minjun.ddd.order.application.port.out;

import minjun.ddd.common.Money;
import minjun.ddd.order.application.port.in.PaymentInfo;

public interface PaymentPort {

  Long createPayment(Long orderId, String cardNo, Money amount);
  Boolean cancelPayment(Long paymentId);
  PaymentInfo getPayment(Long paymentId);
}
