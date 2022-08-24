package minjun.payment.application.port.out;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import minjun.payment.domain.Payment;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class PaymentEvent {

  protected Instant timestamp = Instant.now();
  protected final Payment payment;
}
