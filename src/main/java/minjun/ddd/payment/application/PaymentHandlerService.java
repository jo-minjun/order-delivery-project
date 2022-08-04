package minjun.ddd.payment.application;

import lombok.RequiredArgsConstructor;
import minjun.ddd.common.domain.event.order.OrderCreatedEvent;
import minjun.ddd.payment.domain.Payment;
import minjun.ddd.payment.domain.PaymentRepository;
import minjun.ddd.payment.domain.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentHandlerService {

  private final PaymentService paymentService;
  private final PaymentRepository paymentRepository;

  public void pay(OrderCreatedEvent event) {
    final Payment payment = Payment.createPayment(event);

    paymentService.pay(payment, event.getOrderId());
    paymentRepository.save(payment);
  }
}
