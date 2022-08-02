package minjun.ddd.order.domain.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeliveryCompletedStateTest {

  @Test
  @DisplayName(value = "배송 완료 상태의 value는 40이다.")
  void value() {
    OrderState state = new DeliveryCompletedState();

    Integer value = state.value();

    assertThat(value).isEqualTo(40);
  }
}