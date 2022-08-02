package minjun.ddd.common.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoneyTest {

  @Test
  @DisplayName(value = "10000원 이상은 true이다.")
  void isGreaterThanOrEqualTo() {
    // given
    final Money money1 = new Money(10000);
    final Money money2 = new Money(10000);

    // when
    boolean result = money2.isGreaterThanOrEqualTo(money1);

    // then
    assertThat(result).isTrue();
  }
}