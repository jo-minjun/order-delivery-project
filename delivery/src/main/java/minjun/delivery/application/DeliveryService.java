package minjun.delivery.application;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import minjun.delivery.application.port.in.ChangeDeliveryCommand;
import minjun.delivery.application.port.in.CreateDeliveryCommand;
import minjun.delivery.application.port.in.DeliveryDto;
import minjun.delivery.application.port.in.DeliveryUsecase;
import minjun.delivery.application.port.out.DeliveryCompletedEvent;
import minjun.delivery.application.port.out.DeliveryEventPublisher;
import minjun.delivery.application.port.out.DeliveryRepository;
import minjun.delivery.application.port.out.DeliveryStartedEvent;
import minjun.delivery.domain.Address;
import minjun.delivery.domain.Delivery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public Boolean changeDelivery(Long orderId, ChangeDeliveryCommand command) {
    final Delivery delivery = deliveryRepository.findByOrderId(orderId)
        .orElseThrow(NoSuchElementException::new);

    try {
      delivery.changeDeliveryInfo(new Address(command.getZipCode(), command.getAddress()),
          command.getPhoneNumber());
      return true;
    } catch (RuntimeException e) {
      return false;
    }
  }

  @Override
  public void completeDelivery(Long deliveryId) {
    final Delivery delivery = deliveryRepository.findById(deliveryId)
        .orElseThrow(NoSuchElementException::new);

    delivery.completeDelivery();
    deliveryEventPublisher.publish(new DeliveryCompletedEvent(delivery));
  }

  @Override
  public DeliveryDto getDelivery(Long orderId) {
    final Delivery delivery = deliveryRepository.findByOrderId(orderId)
        .orElseThrow(NoSuchElementException::new);

    final Address address = delivery.getAddress();
    return DeliveryDto.builder()
        .id(delivery.getId())
        .phoneNumber(delivery.getPhoneNumber())
        .address(address.getAddress())
        .zipCode(address.getZipCode())
        .status(delivery.getStatus().name())
        .orderId(delivery.getOrderId())
        .build();
  }
}
