package minjun.delivery.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.delivery.application.DeliveryService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryDomainEventHandler {

  private final DeliveryService deliveryService;

}
