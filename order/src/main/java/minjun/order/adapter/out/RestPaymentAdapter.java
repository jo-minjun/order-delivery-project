package minjun.order.adapter.out;

import lombok.RequiredArgsConstructor;
import minjun.order.application.port.PaymentInfo;
import minjun.order.application.port.out.CreatePaymentCommand;
import minjun.order.application.port.out.PaymentPort;
import minjun.sharedkernel.domain.Money;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestPaymentAdapter implements PaymentPort {

  private final RestTemplate restTemplate = new RestTemplate();
  private static final String PAYMENT_URL = "localhost:8081/api/payment";

  @Override
  public Long createPayment(Long orderId, String cardNo, Money amount) {
    final CreatePaymentCommand command = new CreatePaymentCommand(orderId, cardNo, amount);

    return restTemplate.postForEntity(PAYMENT_URL, command, Long.class).getBody();
  }

  @Override
  public Boolean cancelPayment(Long paymentId) {
    return restTemplate.postForEntity(PAYMENT_URL + "/" + paymentId, null, Boolean.class)
        .getBody();
  }

  @Override
  public PaymentInfo getPayment(Long paymentId) {
    return restTemplate.getForObject(PAYMENT_URL + "/" + paymentId, PaymentInfo.class);
  }
}
