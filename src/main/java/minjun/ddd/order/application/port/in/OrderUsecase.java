package minjun.ddd.order.application.port.in;

public interface OrderUsecase {

  Long placeOrder(PlaceOrderCommand command);
  void changeOrder(Long orderId, ChangeOrderCommand command);
  void startDelivery(Long orderId);
  void completeDelivery(Long orderId);
  void cancelOrder(Long orderId);
}
