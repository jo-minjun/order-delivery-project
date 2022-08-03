package minjun.ddd.delivery.application;

import lombok.RequiredArgsConstructor;
import minjun.ddd.common.domain.event.order.OrderDeliveryInfoChangedEvent;
import minjun.ddd.common.domain.event.order.OrderPaymentApprovedEvent;
import minjun.ddd.delivery.domain.Delivery;
import minjun.ddd.delivery.domain.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryHandlerService {

  private final DeliveryRepository deliveryRepository;

  @Transactional
  public void changeDeliveryInfo(OrderDeliveryInfoChangedEvent event) {
    final Long deliveryId = event.getDeliveryId();
    final Delivery delivery = deliveryRepository.findById(deliveryId)
        .orElseThrow(() -> new RuntimeException("Not Found DeliveryId: " + deliveryId));

    delivery.changeDeliveryInfo(event.getAddress(), event.getPhoneNumber());
  }

  @Transactional
  public void createDelivery(OrderPaymentApprovedEvent event) {
    final Delivery delivery = Delivery.createDelivery(event);
    deliveryRepository.save(delivery);
  }
}
