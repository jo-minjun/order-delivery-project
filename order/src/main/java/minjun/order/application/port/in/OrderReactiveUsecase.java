package minjun.order.application.port.in;

import reactor.core.publisher.Mono;

public interface OrderReactiveUsecase {

  Mono<OrderDto> getOrder(Long orderId);
  Mono<Long> placeOrder(PlaceOrderCommand command);
  void changeOrder(Long orderId, ChangeOrderCommand command);
  void startDelivery(Long orderId);
  void completeDelivery(Long orderId);
  void cancelOrder(Long orderId);
}
