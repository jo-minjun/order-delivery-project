package minjun.delivery.adapter.in;

import lombok.RequiredArgsConstructor;
import minjun.delivery.application.port.in.ChangeDeliveryCommand;
import minjun.delivery.application.port.in.CreateDeliveryCommand;
import minjun.delivery.application.port.in.DeliveryDto;
import minjun.delivery.application.port.in.DeliveryUsecase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/delivery")
public class DeliveryController {

  private final DeliveryUsecase deliveryUsecase;

  @PostMapping
  public Long createDelivery(@RequestBody CreateDeliveryCommand command) {
    return deliveryUsecase.createDelivery(command);
  }

  @PatchMapping("/{deliveryId}/start")
  public void startDelivery(@PathVariable Long deliveryId) {
    deliveryUsecase.startDelivery(deliveryId);
  }

  @PatchMapping("/{deliveryId}/change-info")
  public Boolean changeDeliveryInfo(@PathVariable Long deliveryId, @RequestBody ChangeDeliveryCommand command) {
    return deliveryUsecase.changeDelivery(deliveryId, command);
  }

  @PatchMapping("/{deliveryId}/complete")
  public void completeDelivery(@PathVariable Long deliveryId) {
    deliveryUsecase.completeDelivery(deliveryId);
  }

  @GetMapping("/{deliveryId}")
  public DeliveryDto getDelivery(@PathVariable Long deliveryId) {
    return deliveryUsecase.getDelivery(deliveryId);
  }
}
