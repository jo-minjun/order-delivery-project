package minjun.ddd.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import minjun.ddd.product.domain.Product;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"product", "quantity"})
@ToString(of = {"product", "quantity"})
public class LineItem {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "products_id", nullable = false)
  private Product product;

  @Column(nullable = false)
  private Integer quantity;
}
