package minjun.ddd.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import java.util.Set;
import minjun.ddd.common.domain.Money;
import minjun.ddd.order.domain.state.CancelledState;
import minjun.ddd.order.domain.state.DeliveryStartedState;
import minjun.ddd.order.domain.state.OrderState;
import minjun.ddd.order.domain.state.PaymentApprovedState;
import minjun.ddd.order.domain.state.PlacedState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  @DisplayName(value = "배송이 시작되면 주문을 취소할 수 없다.")
  void cancel_fail() {
    // given
    final OrderState state = new DeliveryStartedState();
    final Order order = createOrderWithStatus(state);

    // when, then
    assertThatThrownBy(order::cancelOrder)
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName(value = "배송이 시작되기 전에는 주문을 취소할 수 있다.")
  void cancel_success() {
    // given
    final OrderState state = new PaymentApprovedState();
    final Order order = createOrderWithStatus(state);

    // when
    order.cancelOrder();

    // then
    assertThat(order.getState())
        .isInstanceOf(CancelledState.class);
  }

  @Test
  @DisplayName(value = "최소 주문 금액 10,000원 미만은 주문할 수 없다.")
  void createOrder_fail() {
    // given
    final OrderLine orderLine = new OrderLine(Set.of(
        createLineItem(1000, 4),
        createLineItem(5999, 1)
    ));

    // when, then
    assertThatThrownBy(() -> Order.createOrder(orderLine, any(Long.class)))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Less than Minimum TotalAmount");
  }

  @Test
  @DisplayName(value = "최소 주문 금액 10,000원 이상은 주문할 수 있다.")
  void createOrder_success() {
    // given
    final OrderLine orderLine = new OrderLine(Set.of(createLineItem(10000, 1)));

    // when
    final Order order = Order.createOrder(orderLine, any(Long.class));

    // then
    assertThat(order.getState())
        .isInstanceOf(PlacedState.class);
  }

  LineItem createLineItem(int price, int quantity) {
    return new LineItem(any(Long.class), new Money(price), quantity);
  }

  Order createOrderWithStatus(OrderState status) {
    return new Order(
        any(Long.class),
        any(OrderLine.class),
        any(Money.class),
        any(Long.class),
        any(Long.class),
        status
    );
  }
}