package minjun.order.adapter.out;

import lombok.RequiredArgsConstructor;
import minjun.order.application.port.PaymentInfo;
import minjun.order.application.port.out.CreatePaymentCommand;
import minjun.order.application.port.out.PaymentReactivePort;
import minjun.sharedkernel.domain.Money;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReactivePaymentAdapter implements PaymentReactivePort {

  private final WebClient webClient = WebClient.create();
  private static final String PAYMENT_URL = "localhost:8081/api/payment";

  @Override
  public Mono<PaymentInfo> createPayment(Long orderId, String cardNo, Money amount) {
    return webClient.post()
        .uri(PAYMENT_URL)
        .bodyValue(new CreatePaymentCommand(orderId, cardNo, amount))
        .retrieve()
        .bodyToMono(PaymentInfo.class);
  }

  @Override
  public Mono<Boolean> cancelPayment(Long orderId) {
    return webClient.post()
        .uri(PAYMENT_URL + "/orders/" + orderId)
        .retrieve()
        .bodyToMono(Boolean.class);
  }

  @Override
  public Mono<PaymentInfo> getPayment(Long orderId) {
    return webClient.get()
        .uri(PAYMENT_URL + "/orders/" + orderId)
        .retrieve()
        .bodyToMono(PaymentInfo.class);
  }
}
