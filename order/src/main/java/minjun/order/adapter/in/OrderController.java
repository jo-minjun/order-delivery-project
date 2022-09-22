package minjun.order.adapter.in;

import lombok.RequiredArgsConstructor;
import minjun.order.application.port.in.ChangeOrderCommand;
import minjun.order.application.port.in.OrderDto;
import minjun.order.application.port.in.OrderUsecase;
import minjun.order.application.port.in.PlaceOrderCommand;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/order")
public class OrderController {

  private final OrderUsecase orderUsecase;

  @GetMapping("/{orderId}")
  public OrderDto getOrder(@PathVariable Long orderId) {
    return orderUsecase.getOrder(orderId);
  }

  @PostMapping
  public Long placeOrder(@RequestBody PlaceOrderCommand command) {
    return orderUsecase.placeOrder(command);
  }

  @PutMapping("/{orderId}")
  public void changeOrder(@PathVariable Long orderId, @RequestBody ChangeOrderCommand command) {
    orderUsecase.changeOrder(orderId, command);
  }

  @DeleteMapping("/{orderId}")
  public void cancelOrder(@PathVariable Long orderId) {
    orderUsecase.cancelOrder(orderId);
  }
}
