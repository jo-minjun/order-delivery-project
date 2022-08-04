package minjun.ddd.order.application.port.out;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import minjun.ddd.order.domain.Order;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class OrderEvent {

  protected Instant timestamp = Instant.now();
  protected final Order order;
}
