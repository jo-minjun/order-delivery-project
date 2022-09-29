package minjun.order.application.port.in;

import reactor.core.publisher.Mono;

public interface OrderUsecase {

  Mono<OrderDto> getOrder(Long orderId);
  Mono<Long> placeOrder(PlaceOrderCommand command);
  Mono<Void> changeOrder(Long orderId, ChangeOrderCommand command);
  Mono<Void> startDelivery(Long orderId);
  Mono<Void> completeDelivery(Long orderId);
  Mono<Void> cancelOrder(Long orderId);
}
