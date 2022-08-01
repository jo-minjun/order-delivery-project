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

  public OrderLine(Set<LineItem> lineItems) {
    this.lineItems = Collections.unmodifiableSet(lineItems);
  }

  public Money calcTotalAmount() {
    int amount = lineItems.stream()
        .mapToInt(lineItem -> lineItem.calcAmount().getValue())
        .sum();

    return new Money(amount);
  }
}
