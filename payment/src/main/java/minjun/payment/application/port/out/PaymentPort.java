package minjun.payment.application.port.out;

import minjun.sharedkernel.domain.Money;

public interface PaymentPort {

  String execute(String cardNo, Money amount);
  Boolean cancel(String authorizedNo);
}
