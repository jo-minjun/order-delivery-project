package minjun.ddd.order.adapter.in;

import lombok.RequiredArgsConstructor;
import minjun.ddd.order.application.port.in.OrderDto;
import minjun.ddd.order.application.port.in.OrderUsecase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderController {

  private final OrderUsecase orderUsecase;

  public OrderDto getOrder(Long orderId) {
    return new OrderDto();
  }
}
