package minjun.payment.adapter.in;

import lombok.RequiredArgsConstructor;
import minjun.payment.application.port.in.CreatePaymentCommand;
import minjun.payment.application.port.in.PaymentDto;
import minjun.payment.application.port.in.PaymentUsecase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

  private final PaymentUsecase paymentUsecase;

  @PostMapping
  public PaymentDto createPayment(@RequestBody CreatePaymentCommand command) {
    return paymentUsecase.createPayment(command);
  }

  @PostMapping("/orders/{orderId}")
  public Boolean cancelPayment(@PathVariable Long orderId) {
    return paymentUsecase.cancelPayment(orderId);
  }

  @GetMapping("/orders/{orderId}")
  public PaymentDto getPayment(@PathVariable Long orderId) {
    return paymentUsecase.getPayment(orderId);
  }
}
