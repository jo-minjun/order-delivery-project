package minjun.delivery.application.port.in;

public interface DeliveryUsecase {

  Long createDelivery(CreateDeliveryCommand command);
  void startDelivery(Long deliveryId);
  Boolean changeDelivery(Long orderId, ChangeDeliveryCommand command);
  void completeDelivery(Long deliveryId);
  DeliveryDto getDelivery(Long orderId);
}
