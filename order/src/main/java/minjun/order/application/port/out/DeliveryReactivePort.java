package minjun.order.application.port.out;

import minjun.order.application.port.DeliveryInfo;
import reactor.core.publisher.Mono;

public interface DeliveryReactivePort {

  Mono<Long> createDelivery(Long orderId, String zipCode, String address, String phoneNumber);
  Mono<Boolean> changeDeliveryInfo(Long deliveryId, String zipCode, String address, String phoneNumber);
  Mono<DeliveryInfo> getDelivery(Long orderId);
}
