package minjun.ddd.order.domain.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeliveryStartedStateTest {

  @Test
  @DisplayName(value = "배송 시작 상태의 value는 30이다.")
  void value() {
    OrderState state = new DeliveryStartedState();

    Integer value = state.value();

    assertThat(value).isEqualTo(30);
  }
}