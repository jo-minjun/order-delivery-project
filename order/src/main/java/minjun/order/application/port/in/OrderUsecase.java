package minjun.order.application.port.in;

public interface OrderUsecase {

  OrderDto getOrder(Long orderId);
  Long placeOrder(PlaceOrderCommand command);
  void changeOrder(Long orderId, ChangeOrderCommand command);
  void startDelivery(Long orderId);
  void completeDelivery(Long orderId);
  void cancelOrder(Long orderId);
}
