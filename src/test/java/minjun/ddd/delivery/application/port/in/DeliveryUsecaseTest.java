package minjun.ddd.delivery.application.port.in;

import static minjun.ddd.Fixture.createPlaceOrderCommand;
import static org.assertj.core.api.Assertions.assertThat;

import minjun.ddd.delivery.domain.DeliveryStatus;
import minjun.ddd.order.application.port.in.DeliveryInfo;
import minjun.ddd.order.application.port.in.OrderDto;
import minjun.ddd.order.application.port.in.OrderUsecase;
import minjun.ddd.order.application.port.in.PlaceOrderCommand;
import minjun.ddd.order.domain.state.DeliveryCompletedState;
import minjun.ddd.order.domain.state.DeliveryStartedState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeliveryUsecaseTest {

  @Autowired
  OrderUsecase orderUsecase;

  @Autowired
  DeliveryUsecase deliveryUsecase;

  @Test
  @DisplayName(value = "배송을 시작하면 Delivery 상태는 STARTED이고 Order 상태는 DELIVERY_STARTED이다.")
  void startDelivery() {
    // Delivery.status = STARTED
    // Order.state = DELIVERY_STARTED

    final PlaceOrderCommand command = createPlaceOrderCommand(true);
    final Long orderId = orderUsecase.placeOrder(command);
    final OrderDto before = orderUsecase.getOrder(orderId);
    final DeliveryInfo beforeDelivery = before.getDelivery();

    deliveryUsecase.startDelivery(beforeDelivery.getDeliveryId());

    final DeliveryDto deliveryDto = deliveryUsecase.getDelivery(beforeDelivery.getDeliveryId());
    final OrderDto after = orderUsecase.getOrder(orderId);

    assertThat(deliveryDto.getStatus()).isEqualTo(DeliveryStatus.STARTED.name());
    assertThat(after.getStatus()).isEqualTo(DeliveryStartedState.class.getSimpleName());
  }

  @Test
  @DisplayName(value = "배송을 완료하면 Delivery 상태는 COMPLETED이고 Order 상태는 DELIVERY_COMPLETED이다.")
  void completeDelivery() {
    // Delivery.status = COMPLETED
    // Order.state = DELIVERY_COMPLETED

    final PlaceOrderCommand command = createPlaceOrderCommand(true);
    final Long orderId = orderUsecase.placeOrder(command);
    final OrderDto before = orderUsecase.getOrder(orderId);
    final DeliveryInfo beforeDelivery = before.getDelivery();
    deliveryUsecase.startDelivery(beforeDelivery.getDeliveryId());

    deliveryUsecase.completeDelivery(beforeDelivery.getDeliveryId());

    final DeliveryDto deliveryDto = deliveryUsecase.getDelivery(beforeDelivery.getDeliveryId());
    final OrderDto after = orderUsecase.getOrder(orderId);

    assertThat(deliveryDto.getStatus()).isEqualTo(DeliveryStatus.COMPLETED.name());
    assertThat(after.getStatus()).isEqualTo(DeliveryCompletedState.class.getSimpleName());
  }
}