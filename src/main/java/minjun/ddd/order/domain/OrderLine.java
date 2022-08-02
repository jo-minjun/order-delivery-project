package minjun.ddd.order.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import minjun.ddd.common.domain.Money;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "lineItems")
@ToString(of = "lineItems")
public class OrderLine {

  @ElementCollection
  @CollectionTable(name = "order_lines", joinColumns = @JoinColumn(name = "orders_id"))
  private Set<LineItem> lineItems = new HashSet<>();

  // order 변경은 없다고 가정한다.
  // 변경 필요시 order 최소 후, 재주문
  // 백오피스에서 변경할 시나리오는 고려하지 않는다.
  // set을 재할당하는 것은 가능
  public OrderLine(Set<LineItem> lineItems) {
    this.lineItems = Collections.unmodifiableSet(lineItems);
  }

  public Money calcTotalAmount() {
    return lineItems.stream()
        .map(LineItem::calcAmount)
        .reduce(Money.ZERO, Money::add);
  }
}
