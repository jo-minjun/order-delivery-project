package minjun.ddd.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import java.util.Set;
import minjun.ddd.common.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

class OrderTest {

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"PLACED", "PAYMENT_APPROVED"},
      mode = Mode.EXCLUDE
  )
  @DisplayName(value = "배송이 시작되면 주문을 취소할 수 없다.")
  void cancel_fail(OrderStatus status) {
    // given
    final Order order = createOrderWithStatus(status);

    // when, then
    assertThatThrownBy(order::cancel)
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Delivery Started");
  }

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"PLACED", "PAYMENT_APPROVED"}
  )
  @DisplayName(value = "배송이 시작되기 전에는 주문을 취소할 수 있다.")
  void cancel_success(OrderStatus status) {
    // given
    final Order order = createOrderWithStatus(status);

    // when
    order.cancel();

    // then
    assertThat(order.getStatus())
        .isEqualTo(OrderStatus.CANCELED);
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
    assertThat(order.getStatus()).isEqualTo(OrderStatus.PLACED);
  }

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"PLACED", "PAYMENT_APPROVED"},
      mode = Mode.EXCLUDE
  )
  @DisplayName(value = "배송이 시작되면 배송지를 변경할 수 없다.")
  void changeDeliveryInfo_fail(OrderStatus status) {
    // given
    final Order order = createOrderWithStatus(status);
    final long deliveryId = 1L;

    // when, then
    assertThatThrownBy(() -> order.changeDeliveryInfo(deliveryId))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Delivery Started");
  }

  @ParameterizedTest
  @EnumSource(
      value = OrderStatus.class,
      names = {"PLACED", "PAYMENT_APPROVED"}
  )
  @DisplayName(value = "배송이 시작되기 전에는 배송지를 변경할 수 있다.")
  void changeDeliveryInfo_success(OrderStatus status) {
    // given
    final Order order = createOrderWithStatus(status);
    final long deliveryId = 1L;

    // when
    order.changeDeliveryInfo(deliveryId);

    // then
    assertThat(order.getDeliveryId()).isEqualTo(deliveryId);
  }

  LineItem createLineItem(int price, int quantity) {
    return new LineItem(any(Long.class), new Money(price), quantity);
  }

  Order createOrderWithStatus(OrderStatus status) {
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