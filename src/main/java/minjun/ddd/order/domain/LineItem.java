package minjun.ddd.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import minjun.ddd.common.Money;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"productId", "quantity"})
@ToString(of = {"productId", "quantity"})
public class LineItem {

  private Long productId;

  // 상품을 구매한 시점의 가격
  private Money price;

  @Column(nullable = false)
  private Integer quantity;

  public LineItem(Long productId, Money price, Integer quantity) {
    this.productId = productId;
    this.price = price;
    this.quantity = quantity;
  }

  Money calcAmount() {
    return price.multiply(quantity);
  }
}
