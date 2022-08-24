package minjun.order.application.port.in;

public interface OrderUsecase {

  OrderDto getOrder(Long orderId);
  Long placeOrder(minjun.order.application.port.in.PlaceOrderCommand command);
  void changeOrder(Long orderId, minjun.order.application.port.in.ChangeOrderCommand command);
  void startDelivery(Long orderId);
  void completeDelivery(Long orderId);
  void cancelOrder(Long orderId);
}
