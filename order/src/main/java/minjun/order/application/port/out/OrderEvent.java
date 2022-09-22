package minjun.order.application.port.out;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import minjun.order.domain.Order;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class OrderEvent {

  protected Instant timestamp = Instant.now();
  protected final Order order;
}
