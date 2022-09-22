package minjun.delivery.application.port.out;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import minjun.delivery.domain.Delivery;

import java.time.Instant;

@Data
@RequiredArgsConstructor
public class DeliveryEvent {

  protected Instant timestamp = Instant.now();
  protected final Delivery delivery;
}
