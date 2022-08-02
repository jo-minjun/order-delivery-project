package minjun.ddd.order.domain.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CancelledStateTest {

  @Test
  @DisplayName(value = "취소 상태의 value는 99이다.")
  void value() {
    OrderState state = new CancelledState();

    Integer value = state.value();

    assertThat(value).isEqualTo(99);
  }
}