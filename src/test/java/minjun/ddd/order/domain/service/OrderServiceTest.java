package minjun.ddd.order.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import minjun.ddd.common.domain.event.DeliveryEvent;
import minjun.ddd.common.domain.event.PaymentEvent;
import minjun.ddd.order.domain.DeliveryInfo;
import minjun.ddd.order.domain.Order;
import minjun.ddd.order.domain.state.DeliveryCompletedState;
import minjun.ddd.order.domain.state.DeliveryStartedState;
import minjun.ddd.order.domain.state.OrderState;
import minjun.ddd.order.domain.state.PaymentApprovedState;
import minjun.ddd.order.domain.state.PlacedState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(value = SpringExtension.class)
class OrderServiceTest {

  @InjectMocks
  OrderService service;

  @Mock
  PaymentEvent paymentEvent;

  @Mock
  DeliveryEvent deliveryEvent;

  @Test
  @DisplayName(value = "DeliveryEvent의 isCompleted가 true이면 주문 상태가 DeliveryCompletedState로 변경되어야 한다.")
  void changeState_delivery_DeliveryStartedState() {
    // given
    Order order = createOrderWithStatus(new DeliveryStartedState());
    given(deliveryEvent.getIsCompleted()).willReturn(true);

    // when
    service.changeState(order, deliveryEvent);

    // then
    assertThat(order.getState())
        .isInstanceOf(DeliveryCompletedState.class);
  }

  @Test
  @DisplayName(value = "DeliveryEvent의 isCompleted가 false이면 주문 상태가 DeliveryStartedState 변경되어야 한다.")
  void changeState_delivery_DeliveryCompletedState() {
    // given
    Order order = createOrderWithStatus(new PaymentApprovedState());
    given(deliveryEvent.getIsCompleted()).willReturn(false);

    // when
    service.changeState(order, deliveryEvent);

    // then
    assertThat(order.getState())
        .isInstanceOf(DeliveryStartedState.class);
  }

  @Test
  @DisplayName(value = "PaymentEvent의 isApproved가 true이면 주문 상태가 PaymentApprovedState로 변경되어야 한다.")
  void changeState_payment_success() {
    // given
    Order order = createOrderWithStatus(new PlacedState());
    given(paymentEvent.getIsApproved()).willReturn(true);

    // when
    service.changeState(order, paymentEvent);

    // then
    assertThat(order.getState())
        .isInstanceOf(PaymentApprovedState.class);
  }

  @Test
  @DisplayName(value = "PaymentEvent의 isApproved가 false이면 주문 상태가 변경되면 안된다.")
  void changeState_payment_fail() {
    // given
    Order order = createOrderWithStatus(new PlacedState());
    given(paymentEvent.getIsApproved()).willReturn(false);

    // when
    service.changeState(order, paymentEvent);

    // then
    assertThat(order.getState())
        .isInstanceOf(PlacedState.class);
  }

  Order createOrderWithStatus(OrderState state) {
    return new Order(
        null,
        null,
        null,
        new DeliveryInfo(null, null, null),
        null,
        state
    );
  }
}