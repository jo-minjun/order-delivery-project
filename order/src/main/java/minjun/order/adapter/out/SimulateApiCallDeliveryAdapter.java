package minjun.order.adapter.out;

import lombok.AllArgsConstructor;
import minjun.delivery.adapter.in.DeliveryController;
import minjun.delivery.application.port.in.DeliveryDto;
import minjun.order.application.port.in.Address;
import minjun.order.application.port.in.DeliveryInfo;
import minjun.order.application.port.out.DeliveryPort;
import org.springframework.stereotype.Component;

@Deprecated
@Component
@AllArgsConstructor
public class SimulateApiCallDeliveryAdapter implements DeliveryPort {

  private final DeliveryController deliveryController = null;

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

  @Override
  public DeliveryInfo getDelivery(Long deliveryId) {
    // Simumate API call
    final DeliveryDto deliveryDto = deliveryController.getDelivery(deliveryId);
    final Address address = Address.builder()
        .address(deliveryDto.getAddress())
        .zipCode(deliveryDto.getZipCode())
        .build();

    return DeliveryInfo.builder()
        .deliveryId(deliveryDto.getId())
        .address(address)
        .phoneNumber(deliveryDto.getPhoneNumber())
        .build();
  }
}
