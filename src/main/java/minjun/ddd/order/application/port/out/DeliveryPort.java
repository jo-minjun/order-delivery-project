package minjun.ddd.order.application.port.out;

public interface DeliveryPort {

  Long createDelivery(Long orderId, String zipCode, String address, String phoneNumber);
  Boolean changeDeliveryInfo(Long deliveryId, String zipCode, String address, String phoneNumber);
}
