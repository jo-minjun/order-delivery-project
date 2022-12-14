package minjun.order.adapter.in;

import lombok.RequiredArgsConstructor;
import minjun.order.application.port.in.ChangeOrderCommand;
import minjun.order.application.port.in.OrderDto;
import minjun.order.application.port.in.OrderUsecase;
import minjun.order.application.port.in.PayOrderCommand;
import minjun.order.application.port.in.PlaceOrderCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/order")
public class OrderController {

  private final OrderUsecase orderUsecase;

  @PostMapping
  public Mono<ResponseEntity<Long>> placeOrder(@RequestBody PlaceOrderCommand command) {
    return orderUsecase.placeOrder(command)
        .map(ResponseEntity::ok);
  }

  @PostMapping("/{orderId}/payment")
  public Mono<ResponseEntity<Boolean>> payOrder(@PathVariable Long orderId, @RequestBody PayOrderCommand command) {
    return orderUsecase.payOrder(orderId, command)
        .map(ResponseEntity::ok);
  }

  @GetMapping("/{orderId}")
  public Mono<ResponseEntity<OrderDto>> getOrder(@PathVariable Long orderId) {
    return orderUsecase.getOrder(orderId)
        .map(ResponseEntity::ok);
  }

  @PatchMapping("/{orderId}")
  public Mono<ResponseEntity<Void>> changeOrder(@PathVariable Long orderId, @RequestBody ChangeOrderCommand command) {
    return orderUsecase.changeOrder(orderId, command)
        .then(Mono.just(ResponseEntity.noContent().build()));
  }

  @DeleteMapping("/{orderId}")
  public Mono<ResponseEntity<Void>> cancelOrder(@PathVariable Long orderId) {
    return orderUsecase.cancelOrder(orderId)
        .then(Mono.just(ResponseEntity.noContent().build()));
  }
}
