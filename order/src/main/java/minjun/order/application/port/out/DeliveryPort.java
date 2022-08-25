package minjun.order.application.port.out;

import minjun.order.application.port.DeliveryInfo;

public interface DeliveryPort {

  Long createDelivery(Long orderId, String zipCode, String address, String phoneNumber);
  Boolean changeDeliveryInfo(Long deliveryId, String zipCode, String address, String phoneNumber);
  DeliveryInfo getDelivery(Long deliveryId);
}
