package minjun.order.adapter.in;

import lombok.RequiredArgsConstructor;
import minjun.order.application.port.in.OrderDto;
import minjun.order.application.port.in.OrderUsecase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderController {

  private final OrderUsecase orderUsecase;

  public OrderDto getOrder(Long orderId) {
    return orderUsecase.getOrder(orderId);
  }
}
