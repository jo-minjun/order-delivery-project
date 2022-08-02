package minjun.ddd.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import minjun.ddd.common.domain.Money;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "orderLine", "totalAmount", "deliveryId", "paymentId"})
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private OrderLine orderLine;

  private Money totalAmount = Money.ZERO;

  private Long deliveryId;

  private Long paymentId;

  // TODO: 상태 패턴
  @Enumerated(value = EnumType.STRING)
  private OrderStatus status = OrderStatus.PLACED;

  /**
   * order를 만저 만들고 delivery를 set할 것인가? delivery를 만들고 order를 생성할 것인가?
   */
  public static Order createOrder(OrderLine orderLine, Long paymentId) {
    final Money totalAmount = orderLine.calcTotalAmount();
    verifyMinimumTotalAmount(totalAmount);

    return new Order(orderLine, totalAmount, paymentId);
  }

  private Order(OrderLine orderLine, Money totalAmount, Long paymentId) {
    this.orderLine = orderLine;
    this.totalAmount = totalAmount;
    this.paymentId = paymentId;
  }

  public void cancel() {
    verifyNotYetDeliveryStarted(status);
    status = OrderStatus.CANCELED;
  }

  public void changeDeliveryInfo(Long deliveryId) {
    verifyNotYetDeliveryStarted(status);
    this.deliveryId = deliveryId;
  }

  private static void verifyNotYetDeliveryStarted(OrderStatus status) {
    if (!status.isNotYetDeliveryStarted()) {
      throw new RuntimeException("Delivery Started");
    }
  }

  private static void verifyMinimumTotalAmount(Money totalAmount) {
    final Money minimumOrderAmount = new Money(10000);

    if (!totalAmount.isGreaterThanOrEqualTo(minimumOrderAmount)) {
      throw new RuntimeException("Less than Minimum TotalAmount");
    }
  }
}
