package minjun.delivery.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.delivery.application.port.in.DeliveryUsecase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryDomainEventHandler {

  private final DeliveryUsecase deliveryUsecase;

}
