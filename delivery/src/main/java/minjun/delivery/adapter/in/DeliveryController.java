package minjun.delivery.adapter.in;

import lombok.RequiredArgsConstructor;
import minjun.delivery.application.port.in.ChangeDeliveryCommand;
import minjun.delivery.application.port.in.CreateDeliveryCommand;
import minjun.delivery.application.port.in.DeliveryDto;
import minjun.delivery.application.port.in.DeliveryUsecase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery")
public class DeliveryController {

  private final DeliveryUsecase deliveryUsecase;

  @PostMapping
  public void createDelivery(@RequestBody CreateDeliveryCommand command) {
    deliveryUsecase.createDelivery(command);
  }

  @PostMapping("/{deliveryId}/start")
  public void startDelivery(@PathVariable Long deliveryId) {
    deliveryUsecase.startDelivery(deliveryId);
  }

  @PostMapping("/orders/{orderId}/change-info")
  public Boolean changeDeliveryInfo(@PathVariable Long orderId, @RequestBody ChangeDeliveryCommand command) {
    return deliveryUsecase.changeDelivery(orderId, command);
  }

  @PostMapping("/{deliveryId}/complete")
  public void completeDelivery(@PathVariable Long deliveryId) {
    deliveryUsecase.completeDelivery(deliveryId);
  }

  @GetMapping("/orders/{orderId}")
  public DeliveryDto getDelivery(@PathVariable Long orderId) {
    return deliveryUsecase.getDelivery(orderId);
  }
}
