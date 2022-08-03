package minjun.ddd.delivery.application;

import lombok.RequiredArgsConstructor;
import minjun.ddd.delivery.domain.Delivery;
import minjun.ddd.delivery.domain.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryAppService {

  private final DeliveryRepository deliveryRepository;

  @Transactional
  public void startDelivery(Long deliveryId, Long orderId) {
    final Delivery delivery = deliveryRepository.findById(deliveryId)
        .orElseThrow(RuntimeException::new);

    delivery.startDelivery(orderId);
    deliveryRepository.save(delivery);
  }

  @Transactional
  public void completeDelivery(Long deliveryId, Long orderId) {
    final Delivery delivery = deliveryRepository.findById(deliveryId)
        .orElseThrow(RuntimeException::new);

    delivery.completeDelivery(orderId);
    deliveryRepository.save(delivery);
  }
}
