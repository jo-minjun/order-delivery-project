package minjun.ddd.order.adapter.out;

import lombok.AllArgsConstructor;
import minjun.ddd.delivery.adapter.in.DeliveryController;
import minjun.ddd.order.application.port.out.DeliveryPort;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SimulateApiCallDeliveryAdapter implements DeliveryPort {

  private final DeliveryController deliveryController;

  @Override
  public Long createDelivery(Long orderId, String zipCode, String address, String phoneNumber) {
    // Simumate API call
    return deliveryController.createDelivery(orderId, zipCode, address, phoneNumber);
  }

  @Override
  public Boolean changeDeliveryInfo(Long deliveryId, String zipCode, String address, String phoneNumber) {
    // Simumate API call
    return deliveryController.changeDelivery(deliveryId, zipCode, address, phoneNumber);
  }
}
