package minjun.order.application.port.out;

import minjun.order.application.port.PaymentInfo;
import minjun.sharedkernel.domain.Money;
import reactor.core.publisher.Mono;

public interface PaymentReactivePort {

  Mono<PaymentInfo> createPayment(Long orderId, String cardNo, Money amount);
  Mono<Boolean> cancelPayment(Long orderId);
  Mono<PaymentInfo> getPayment(Long orderId);
}
