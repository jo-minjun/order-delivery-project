package minjun.order.application.port.in;

import reactor.core.publisher.Mono;

public interface OrderUsecase {

  Mono<Long> placeOrder(PlaceOrderCommand command);
  Mono<Boolean> payOrder(Long orderId, PayOrderCommand command);
  Mono<OrderDto> getOrder(Long orderId);
  Mono<Void> changeOrder(Long orderId, ChangeOrderCommand command);
  Mono<Void> startDelivery(Long orderId);
  Mono<Void> completeDelivery(Long orderId);
  Mono<Void> cancelOrder(Long orderId);
}
