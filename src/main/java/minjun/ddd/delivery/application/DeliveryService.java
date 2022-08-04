package minjun.ddd.delivery.application;

import lombok.RequiredArgsConstructor;
import minjun.ddd.delivery.application.port.in.*;
import minjun.ddd.delivery.application.port.out.*;
import minjun.ddd.delivery.domain.Address;
import minjun.ddd.delivery.domain.Delivery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService implements DeliveryUsecase {

  private final DeliveryRepository deliveryRepository;
  private final DeliveryEventPublisher deliveryEventPublisher;

  @Override
  public Long createDelivery(CreateDeliveryCommand command) {
    final Delivery delivery = Delivery.createDelivery(command.getOrderId(),
        new Address(command.getZipCode(), command.getAddress()), command.getPhoneNumber());

    deliveryRepository.save(delivery);
    deliveryEventPublisher.publish(new DeliveryCreatedEvent(delivery));

    return delivery.getId();
  }

  @Override
  public void startDelivery(Long deliveryId) {
    final Delivery delivery = deliveryRepository.findById(deliveryId)
        .orElseThrow(NoSuchElementException::new);

    delivery.startDelivery();
    deliveryEventPublisher.publish(new DeliveryStartedEvent(delivery));
  }

  @Override
  public Boolean changeDelivery(Long deliveryId, ChangeDeliveryCommand command) {
    final Delivery delivery = deliveryRepository.findById(deliveryId)
        .orElseThrow(() -> new NoSuchElementException());

    delivery.changeDeliveryInfo(new Address(command.getZipCode(), command.getAddress()), command.getPhoneNumber());

    return true;
  }

  @Override
  public void completeDelivery(Long deliveryId) {
    final Delivery delivery = deliveryRepository.findById(deliveryId)
        .orElseThrow(NoSuchElementException::new);

    delivery.completeDelivery();
    deliveryEventPublisher.publish(new DeliveryCompletedEvent(delivery));
  }
}
