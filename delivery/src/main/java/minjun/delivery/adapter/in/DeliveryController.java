package minjun.delivery.adapter.in;

import lombok.RequiredArgsConstructor;
import minjun.delivery.application.port.in.ChangeDeliveryCommand;
import minjun.delivery.application.port.in.CreateDeliveryCommand;
import minjun.delivery.application.port.in.DeliveryDto;
import minjun.delivery.application.port.in.DeliveryUsecase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryController {

  private final DeliveryUsecase deliveryUsecase;

  public Long createDelivery(Long orderId, String zipCode, String address, String phoneNumber) {
    return deliveryUsecase.createDelivery(new CreateDeliveryCommand(orderId, zipCode, address, phoneNumber));
  }

  public void startDelivery(Long deliveryId) {
    deliveryUsecase.startDelivery(deliveryId);
  }

  public Boolean changeDelivery(Long deliveryId, String zipCode, String address, String phoneNumber) {
    return deliveryUsecase.changeDelivery(deliveryId, new ChangeDeliveryCommand(zipCode, address, phoneNumber));
  }

  public void completeDelivery(Long deliveryId) {
    deliveryUsecase.completeDelivery(deliveryId);
  }

  public DeliveryDto getDelivery(Long deliveryId) {
    return deliveryUsecase.getDelivery(deliveryId);
  }
}
