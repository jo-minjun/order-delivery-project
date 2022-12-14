package minjun.order.domain;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import minjun.sharedkernel.domain.Money;
import minjun.sharedkernel.domain.MoneyConverter;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"productId", "quantity"})
@ToString(of = {"productId", "quantity"})
public class LineItem {

  private Long productId;

  private String productName;

  // 상품을 구매한 시점의 가격
  @Convert(converter = MoneyConverter.class)
  private Money price;

  @Column(nullable = false)
  private Integer quantity;

  public LineItem(Long productId, String productName, Money price, Integer quantity) {
    this.productId = productId;
    this.productName = productName;
    this.price = price;
    this.quantity = quantity;
  }

  Money calcAmount() {
    return price.multiply(quantity);
  }
}
