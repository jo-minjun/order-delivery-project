package minjun.ddd.order.application.port.out;

import minjun.ddd.order.application.port.in.DeliveryInfo;

public interface DeliveryPort {

  Long createDelivery(Long orderId, String zipCode, String address, String phoneNumber);
  Boolean changeDeliveryInfo(Long deliveryId, String zipCode, String address, String phoneNumber);
  DeliveryInfo getDelivery(Long deliveryId);
}
