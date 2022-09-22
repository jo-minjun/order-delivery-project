package minjun.order.adapter.in;

import lombok.RequiredArgsConstructor;
import minjun.order.application.port.in.OrderDto;
import minjun.order.application.port.in.OrderReactiveUsecase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class OrderReactiveController {

  private final OrderReactiveUsecase orderUsecase;

  @GetMapping("/hello")
  public String hello() {
    return "hello";
  }

  @GetMapping("/{orderId}")
  public Mono<OrderDto> getOrder(@PathVariable Long orderId) {
    return orderUsecase.getOrder(orderId);
  }
}
