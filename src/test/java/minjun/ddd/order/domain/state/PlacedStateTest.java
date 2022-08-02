package minjun.ddd.order.domain.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import minjun.ddd.order.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlacedStateTest {

  @Test
  @DisplayName(value = "주문됨 상태의 value는 10이다.")
  void value() {
    OrderState state = new PlacedState();

    Integer value = state.value();

    assertThat(value).isEqualTo(10);
  }

  @Test
  @DisplayName(value = "주문됨 상태는 배송지 정보를 변경할 수 있다.")
  void canChangeDeliveryInfo() {
    OrderState state = new PlacedState();

    Boolean canChangeDeliveryInfo = state.canChangeDeliveryInfo();

    assertThat(canChangeDeliveryInfo).isTrue();
  }

  @Test
  @DisplayName(value = "주문됨 상태는 주문을 취소할 수 있다.")
  void orderCanceled() {
    Order order = new Order(null, null, null, 1L, null, new PlacedState());

    order.cancelOrder();

    assertThat(order.getState()).isInstanceOf(CancelledState.class);
  }

}