package minjun.ddd.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
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
    Order order = createOrderWithStatus(status);

    // when, then
    assertThatThrownBy(() -> order.cancel())
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
    Order order = createOrderWithStatus(status);

    // when
    order.cancel();

    // then
    assertThat(order.getStatus())
        .isEqualTo(OrderStatus.CANCELED);
  }

  Order createOrderWithStatus(OrderStatus status) {
    return Order.builder()
        .status(status)
        .build();
  }
}