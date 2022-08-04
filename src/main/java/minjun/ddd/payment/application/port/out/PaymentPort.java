package minjun.ddd.payment.application.port.out;

import minjun.ddd.common.Money;

public interface PaymentPort {

  String execute(String cardNo, Money amount);
  Boolean cancel(String authorizedNo);
}
