package minjun.ddd.order.application.port.out;

import minjun.ddd.common.Money;

public interface PaymentPort {

  Long createPayment(Long orderId, String cardNo, Money amount);
  Boolean cancelPayment(Long paymentId);
}
