package minjun.ddd.payment.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.ddd.payment.application.PaymentService;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentDomainEventHandler {

  private final PaymentService paymentService;
}

