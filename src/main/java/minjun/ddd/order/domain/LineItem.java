package minjun.ddd.order.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import minjun.ddd.common.domain.Money;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"productId", "quantity"})
@ToString(of = {"productId", "quantity"})
public class LineItem {

  private Long productId;

  @Embedded
  @AttributeOverrides(value = {
      @AttributeOverride(name = "value", column = @Column(name = "price", nullable = false))
  })
  private Money price;

  @Column(nullable = false)
  private Integer quantity;

  @Builder
  public LineItem(Long productId, Money price, Integer quantity) {
    this.productId = productId;
    this.price = price;
    this.quantity = quantity;
  }

  public Money calcAmount() {
    return price.multiple(quantity);
  }
}
